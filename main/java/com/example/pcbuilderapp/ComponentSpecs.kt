package com.example.pcbuilderapp

data class ComponentSpecs(
    val id: Int,
    val type: String,
    val name: String,
    val specs: Map<String, String>,
    val tdp: Int
)
