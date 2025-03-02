package com.example.fetchcodingexercise.data.models

/**
 * Data class that only contains the data structure fetched from the raw JSON. Note that the name can be null
 */
data class ItemCoreData(
    val id: Int,
    val listId: Int,
    val name: String?
)
