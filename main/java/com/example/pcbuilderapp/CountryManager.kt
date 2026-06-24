package com.example.pcbuilderapp

object CountryManager {
    val countries = listOf (
        CountryEntity("Latvia", "LV", "€"),
        CountryEntity("United States", "US", "$")
    )
    fun getRegion(prefs: android.content.SharedPreferences) : CountryEntity {
        val code = prefs.getString("country","LV") ?: "LV"
        return countries.firstOrNull { it.code == code } ?: countries.first()
    }
}