package com.nirmal.baby.grocy.data.model

data class GroceryItem(
    val name: String,
    val quantity: String,
    val unit: String,
    val expiryDate: String,
    val price: Double?,
    val description: String
)
