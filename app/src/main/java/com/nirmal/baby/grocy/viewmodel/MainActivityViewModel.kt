package com.nirmal.baby.grocy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nirmal.baby.grocy.data.model.GroceryItem
import com.nirmal.baby.grocy.data.repository.GroceryRepository

class MainActivityViewModel(private val repository: GroceryRepository) : ViewModel() {

    fun getGroceryList(): LiveData<List<GroceryItem>> = repository.getGroceryList()

    fun addGroceryItem(item: GroceryItem) {
        repository.addGroceryItem(item)
    }
}