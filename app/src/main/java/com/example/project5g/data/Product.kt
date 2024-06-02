package com.example.project5g.data

data class Product(
    var id: String? = null,
    var categoryId: String? = null,
    var supplierId: String? = null,
    var barcode: String? = null,
    var name: String? = null,
    var price: Int? = null,
    var stock: Int? = null,
    var ws_price: Int? = null,
    var ws: Int? = null,
    var image: String? = null,
    var imageURL: String? = null,
    var category: String? = null,
    var supplier: String? = null
)
