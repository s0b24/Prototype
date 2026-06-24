package com.example.pcbuilderapp

object PriceFormatter {
    fun format(price: Double?, country: CountryEntity): String {
        if (price == null) return ""
        return when (country.currency) {
            "€" -> "%.2f €".format(price)
            "$" -> "%.2f $".format(price)
            else -> "%.2f %s".format(price, country.currency)
        }
    }
}