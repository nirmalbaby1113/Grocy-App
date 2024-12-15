package com.nirmal.baby.grocy.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nirmal.baby.grocy.data.model.GroceryItem

class GroceryRepository {

    private val groceryList = MutableLiveData<List<GroceryItem>>(emptyList())

    fun getGroceryList(): LiveData<List<GroceryItem>> = groceryList

    fun addGroceryItem(item: GroceryItem) {
        groceryList.value = groceryList.value?.plus(item)
    }
}