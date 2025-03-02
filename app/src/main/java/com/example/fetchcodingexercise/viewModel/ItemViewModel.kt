package com.example.fetchcodingexercise.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchcodingexercise.R
import com.example.fetchcodingexercise.data.api.RetrofitInstance
import com.example.fetchcodingexercise.data.models.Item
import kotlinx.coroutines.launch

/**
 * ViewModel that holds and manages the state of items fetched from the server
 */
class ItemViewModel : ViewModel() {
    // Holds the private state of items
    private val _items = mutableStateOf<List<Item>>(emptyList())

    // Holds the private state for loading
    private val _isLoading = mutableStateOf(false)

    // Publicly viewable state list of items
    val items: State<List<Item>> = _items

    // Publicly viewable state of isLoading
    val isLoading: State<Boolean> = _isLoading

    // On creation of the ViewModel, fetch items
    init {
        fetchItems()
    }

    /**
     * Fetches the items from the server by calling retrofit's generated getItems call
     */
    fun fetchItems() {
        // async call
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Get the ItemCoreData from the retrofit instance and map them to regular Items
                _items.value = RetrofitInstance.api.getItems().mapNotNull { item ->
                    item.name?.let { name ->
                        Item(
                            id = item.id,
                            listId = item.listId,
                            name = name,
                            itemImageResId = getRandomImage()
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("ApiError", e.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }
}

/**
 * Gets a random image id from a list of images
 */
private fun getRandomImage(): Int {
    val imagesToPickFrom = listOf(
        R.drawable.baseline_earbuds,
        R.drawable.baseline_weekend,
        R.drawable.baseline_computer,
        R.drawable.baseline_dry_cleaning,
        R.drawable.baseline_fitness_center
    )

    return imagesToPickFrom.random()
}