package com.example.pcbuilderapp

import android.content.Context

object DatabaseProvider {
    private var instance: DatabaseHelper? = null

    fun get(context: Context): DatabaseHelper {
        if (instance == null) {
            instance = DatabaseHelper(context.applicationContext)
            instance!!.copyDatabase()
        }
        return instance!!
    }
}