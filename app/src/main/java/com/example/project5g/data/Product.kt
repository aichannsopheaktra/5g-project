package com.example.project5g.data

data class Product(
    var id: String? = null,
    var categoryId: String? = null,
    var supplierId: String? = null,
    var barcode: String? = null,
    var name: String? = null,
    val price: Float,  // Changed from Int to Float
    val stock: Int,
    val ws_price: Float,  // Changed from Int to Float
    val ws: Float,  // Changed from Int to Float
    var image: String? = null,
    var imageURL: String? = null,
    var category: String? = null,
    var supplier: String? = null
)
