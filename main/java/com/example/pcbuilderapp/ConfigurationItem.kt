package com.example.pcbuilderapp

data class ConfigurationItem(
    val type: String,
    val name: String,
    var componentId: Int? = null,
    var selectedComponents: String? = null,
    var price: Double? = null,
    var tdp: Int? = null,
)