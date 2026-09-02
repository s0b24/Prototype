package com.example.pcbuilderapp

object CountryManager {
    val countries = listOf (
        CountryEntity("Latvia", "LV", "€", "lv"),
        CountryEntity("United States", "US", "$", "en")
    )
    fun getCountry(sharedPreferences: android.content.SharedPreferences) : CountryEntity {
        val code = sharedPreferences.getString("country","US") ?: "US"
        return countries.firstOrNull { it.code == code } ?: countries.first()
    }
}