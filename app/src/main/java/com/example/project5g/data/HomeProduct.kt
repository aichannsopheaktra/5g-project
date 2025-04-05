package com.example.project5g.data

data class HomeProduct(
    val id: String,
    val categoryId: String,
    val supplierId: String,
    val barcode: String,
    val name: String,
    val price: Float, // Change to Float
    val stock: Int,
    val ws_price: Float, // Change to Float
    val ws: Float, // Change to Float
    val image: String?,
    val imageURL: String,
    val createDate: String,
    val discount: Int,
    val category: Categories,
    val supplier: Any?
)
data class Category(
    var id: String? = null,
    var name: String? = null
)
