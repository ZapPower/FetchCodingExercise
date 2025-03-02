package com.example.fetchcodingexercise.data.models

/**
 * Full item data class. The Image ID is added and null name is removed from ItemCoreData
 */
data class Item(
    val name: String,
    val id: Int,
    val listId: Int,
    val itemImageResId: Int
)
