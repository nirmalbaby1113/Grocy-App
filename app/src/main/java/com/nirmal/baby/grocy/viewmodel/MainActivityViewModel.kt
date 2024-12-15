package com.nirmal.baby.grocy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.nirmal.baby.grocy.data.model.GroceryItem
import com.nirmal.baby.grocy.data.repository.GroceryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: GroceryRepository
) : ViewModel() {

    fun getGroceryList(): LiveData<List<GroceryItem>> = repository.getGroceryList()

    fun addGroceryItem(item: GroceryItem, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        repository.addGroceryItem(item, onSuccess, onFailure)
    }
}