package com.example.pcbuilderapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, "components.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        // already created
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun copyDatabase() {
        val dbFile = context.getDatabasePath("components.db")

        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()
            val input = context.assets.open("components.db")
            val output = FileOutputStream(dbFile)
            input.copyTo(output)

            input.close()
            output.close()
        }
    }

    fun getComponentById(id: Int): ComponentEntity {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
                SELECT id, name, type
                FROM COMPONENTS
                WHERE id = ?
            """, arrayOf(id.toString())
        )
        var component: ComponentEntity? = null

        if (cursor.moveToFirst()) {
            component = ComponentEntity(
                id = cursor.getInt(0),
                name = cursor.getString(1),
                type = cursor.getString(2),
            )
        }
        cursor.close()
        return component ?: throw IllegalArgumentException("Komponents nav atrasts")
    }

    fun getComponentByType(type: String): List<ComponentEntity> {
        val components = mutableListOf<ComponentEntity>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
                SELECT id, name, type
                FROM COMPONENTS
                WHERE type = ?
            """, arrayOf(type)
        )

        while (cursor.moveToNext()) {
            components.add(ComponentEntity(
                id = cursor.getInt(0),
                name = cursor.getString(1),
                type = cursor.getString(2),
            ))
        }
        cursor.close()
        return components
    }

    fun getComponentSpecs(componentId: Int): Map<String, String> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
                SELECT spec_key, spec_value
                FROM SPECIFICATIONS
                WHERE component_id = ?
            """, arrayOf(componentId.toString())
        )
        val specs = mutableMapOf<String, String>()

        while (cursor.moveToNext()) {
            val key = cursor.getString(0)
            val value = cursor.getString(1)
            specs[key] = value
        }
        cursor.close()
        return specs
    }

    fun getAveragePriceByRegion(componentId: Int, regionCode: String): Double? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
                SELECT AVG(price)
                FROM PRICES p
                JOIN STORES s ON p.store_id = s.id
                WHERE p.component_id = ? AND s.country = ? AND p.price IS NOT NULL
            """, arrayOf(componentId.toString(), regionCode)
        )
        var price: Double? = null

        if (cursor.moveToFirst()) {
            if (!cursor.isNull(0)) {
                price = cursor.getDouble(0)
            }
        }
        cursor.close()
        return price
    }

    fun getComponentCard(type: String, regionCode: String): List<ComponentCard> {
        val result = mutableListOf<ComponentCard>()
        val components = getComponentByType(type)

        for (c in components) {
            val specifications = getComponentSpecs(c.id)
            val price = getAveragePriceByRegion(c.id, regionCode)

            result.add(ComponentCard(
                id = c.id,
                name = c.name,
                type = c.type,
                averagePrice = price,
                specs = specifications
            ))
        }
        return result
    }

    fun getStorePrices(componentId: Int, regionCode: String) : List<StorePriceEntity> {
        val result = mutableListOf<StorePriceEntity>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
                SELECT s.name, p.price, p.currency
                FROM PRICES p
                JOIN STORES s ON p.store_id = s.id
                WHERE p.component_id = ? AND s.country = ? AND p.price IS NOT NULL
                ORDER BY p.price ASC
            """, arrayOf(componentId.toString(), regionCode)
        )

        while (cursor.moveToNext()) {
            result.add(StorePriceEntity(
                name = cursor.getString(0),
                price = if (cursor.isNull(1)) null else cursor.getDouble(1),
                currency = cursor.getString(2)
            ))
        }
        cursor.close()
        return result
    }

    fun saveConfiguration(configurationId: Int, selected: Map<String, ComponentSpecs>, totalPrice: Double) {
        val db = writableDatabase

        if (configurationId != -1) {
            db.execSQL(
                """
                    DELETE FROM CONFIGURATION_ITEMS
                    WHERE configuration_id = ?
                """, arrayOf(configurationId)
            )

            selected.values.forEach {
                db.execSQL(
                    "INSERT INTO CONFIGURATION_ITEMS (configuration_id, component_type, component_id) VALUES (?, ?, ?)",
                    arrayOf(configurationId, it.type, it.id)
                )
            }
            return
        }

        val cursor = db.rawQuery(
            """
                SELECT COUNT(*)
                FROM CONFIGURATIONS
            """, null
        )
        cursor.moveToFirst()
        val nextNum = cursor.getInt(0) + 1
        cursor.close()

        val name = "Konfigurācija $nextNum"

        val cs = db.compileStatement(
            "INSERT INTO CONFIGURATIONS(name, total_price) VALUES(?, ?)"
        )
        cs.bindString(1, name)
        cs.bindDouble(2, totalPrice)

        val newConfigurationId = cs.executeInsert()

        selected.values.forEach {
            db.execSQL(
                "INSERT INTO CONFIGURATION_ITEMS (configuration_id, component_type, component_id) VALUES (?, ?, ?)",
                arrayOf(newConfigurationId, it.type, it.id)
            )
        }
    }

    fun getSavedConfigurations(): List<SavedConfiguration> {
        val result = mutableListOf<SavedConfiguration>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
                SELECT id, name, total_price
                FROM CONFIGURATIONS
            """, null
        )
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val price = cursor.getDouble(2)

            val componentsCursor = db.rawQuery(
                """
                    SELECT c.name
                    FROM CONFIGURATION_ITEMS ci
                    JOIN COMPONENTS c ON c.id = ci.component_id
                    WHERE ci.configuration_id = ?
                """, arrayOf(id.toString())
            )
            val components = mutableListOf<String>()

            while (componentsCursor.moveToNext()) {
                components.add(componentsCursor.getString(0))
            }
            componentsCursor.close()

            result.add(SavedConfiguration(
                id = id,
                name = name,
                components = components.joinToString("\n"),
                totalPrice = price
            ))
        }
        cursor.close()
        return result
    }

    fun deleteConfiguration(configurationId: Int) {
        writableDatabase.execSQL(
            """
                DELETE FROM CONFIGURATION_ITEMS
                WHERE configuration_id = ?
            """, arrayOf(configurationId)
        )

        writableDatabase.execSQL(
            """
                DELETE FROM CONFIGURATIONS
                WHERE id = ?
            """, arrayOf(configurationId)
        )
    }

    fun getSavedConfigurationsItems(configurationId: Int): List<ComponentSpecs> {
        val result = mutableListOf<ComponentSpecs>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
                SELECT component_id
                FROM CONFIGURATION_ITEMS
                WHERE configuration_id = ?
            """, arrayOf(configurationId.toString())
        )

        while (cursor.moveToNext()) {
            val componentId = cursor.getInt(0)
            val component = getComponentById(componentId)
            val specs = getComponentSpecs(componentId)

            result.add(ComponentSpecs(
                id = component.id,
                type = component.type,
                name = component.name,
                specs = specs,
                tdp = specs["TDP (W)"]?.toIntOrNull() ?: 0
            ))
        }
        cursor.close()
        return result
    }
}
