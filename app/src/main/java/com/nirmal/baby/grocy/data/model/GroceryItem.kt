package com.nirmal.baby.grocy.data.model

data class GroceryItem(
    var id: String? = null,
    var name: String = "",
    var quantity: String = "",
    var unit: String = "",
    var expiryDate: String? = "",
    var price: Double? = null,
    var description: String = ""
) {
    constructor() : this(null, "", "", "", "", null, "")
}
