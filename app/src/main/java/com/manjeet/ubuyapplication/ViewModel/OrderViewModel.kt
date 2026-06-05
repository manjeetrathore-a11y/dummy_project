package com.manjeet.ubuyapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manjeet.ubuyapplication.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Screen state track
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadOrdersFromAssets()
    }

    // 2. Local Asset Folder se JSON parsing functional engine logic
    private fun loadOrdersFromAssets() {
        viewModelScope.launch {
            _isLoading.value = true

            val parsedList = withContext(Dispatchers.IO) {
                try {
                    val jsonString = getApplication<Application>().assets
                        .open("orders.json")
                        .bufferedReader()
                        .use { it.readText() }

                    // Gson Tokenizer setup to parse array list arrays safely
                    val itemType = object : TypeToken<List<Order>>() {}.type
                    Gson().fromJson<List<Order>>(jsonString, itemType)
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList<Order>()
                }
            }
            _orders.value = parsedList
            _isLoading.value = false
        }
    }
}