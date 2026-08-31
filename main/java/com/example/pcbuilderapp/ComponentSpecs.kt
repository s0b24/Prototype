package com.example.pcbuilderapp

import java.io.Serializable

data class ComponentSpecs(
    val id: Int,
    val type: String,
    val name: String,
    val specs: Map<String, String>,
    val tdp: Int
) : Serializable
