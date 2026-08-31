package com.example.pcbuilderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FilterComponentsActivity : AppCompatActivity() {
    private lateinit var adapter: FilterComponentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_components)

        val type = intent.getStringExtra("type") ?: ""
        val country = CountryManager.getCountry(getSharedPreferences("CountryPreferences", MODE_PRIVATE))
        val db = DatabaseProvider.get(this)
        val componentCard = db.getComponentCard(type, country.code)
        val filterSettings = SpecsPriority.getPrioritySpecifications(type)
        val filters = mutableMapOf<String, List<String>>()

        // Atlases parametru apstrāde: dublikātu dzēšana, parametru šķirošana (Int - augošā secībā, String - alfabēta secībā)
        filterSettings.forEach { specsName ->
            val specsValue = componentCard.mapNotNull { item ->
                item.specs[specsName]
            }.distinct()

            val sortedValues =
                if (specsValue.all { it.toIntOrNull() != null }) {
                    specsValue.sortedBy { it.toInt() }
                } else {
                    specsValue.sorted()
                }

            if (sortedValues.isNotEmpty()) {
                filters[specsName] = sortedValues
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFilterComponentsActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FilterComponentsAdapter(filters)
        recyclerView.adapter = adapter

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val applyBtn = findViewById<Button>(R.id.applyBtn)
        applyBtn.setOnClickListener {
            val minPrice = findViewById<EditText>(R.id.minPriceValue).text.toString().toDoubleOrNull()
            val maxPrice = findViewById<EditText>(R.id.maxPriceValue).text.toString().toDoubleOrNull()
            val intent = Intent()
            intent.putExtra("minPrice", minPrice)
            intent.putExtra("maxPrice", maxPrice)
            intent.putExtra("filters", adapter.getSelectedFilters())
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}