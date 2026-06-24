package com.example.pcbuilderapp

data class ComponentCard(
    val id: Int,
    val type: String,
    val name: String,
    val specs: Map<String, String>,
    val averagePrice: Double?
)