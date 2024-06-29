package com.example.project5g.data

data class Product(
    val id: String? = null,
    val categoryId: String? = null,
    val supplierId: String? = null,
    val barcode: String? = null,
    val name: String? = null,
    val price: Float,
    val stock: Int,
    val ws_price: Float,
    val ws: Float,
    var image: String? = null,
    var imageURL: String? = null,
    var category: String? = null,
    var supplier: String? = null
)
