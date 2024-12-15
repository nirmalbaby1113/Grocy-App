package com.nirmal.baby.grocy.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.nirmal.baby.grocy.data.model.GroceryItem

class GroceryRepository {

    private val db = FirebaseFirestore.getInstance()
    private val groceryListLiveData = MutableLiveData<List<GroceryItem>>()

    init{
        db.collection("groceries")
            .addSnapshotListener {snapshot, exception ->
                if (exception != null) {
                    Log.e("GroceryRepository", "Error fetching data", exception)
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val groceries = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(GroceryItem::class.java)?.copy(id = doc.id)
                    }
                    Log.d("GroceryRepository", "Groceries fetched: $groceries")
                    groceryListLiveData.postValue(groceries)
                } else {
                    Log.d("GroceryRepository", "No groceries found")
                    groceryListLiveData.postValue(emptyList())
                }

            }
    }

    fun getGroceryList(): LiveData<List<GroceryItem>> = groceryListLiveData

    fun addGroceryItem(item: GroceryItem, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("groceries")
            .add(item)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}