package com.example.project5g.data

data class HomeProduct(
    val id: String,
    val categoryId: String,
    val supplierId: String,
    val barcode: String,
    val name: String,
    val price: Int,
    val stock: Int,
    val ws_price: Int,
    val ws: Int,
    val image: String?,
    val imageURL: String,
    val createDate: String,
    val discount: Int,
    val category: Categories,
    val supplier: Any? // Assuming supplier can be any type or null
)

