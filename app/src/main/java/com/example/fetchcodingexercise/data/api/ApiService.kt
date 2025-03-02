package com.example.fetchcodingexercise.data.api

import com.example.fetchcodingexercise.data.models.ItemCoreData
import retrofit2.http.GET

/**
 * Defines the GET call for the item data to Retrofit
 */
interface ApiService {
    @GET("hiring.json")
    suspend fun getItems(): List<ItemCoreData>
}