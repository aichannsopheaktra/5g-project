package com.example.project5g.data

import java.util.Objects

data class HomeProduct(
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
    var createDate: String?=null,
    var discount: Int? =null,
    var category: Category?,
    var supplier: String? = null
)
data class Category(
    var id: String? = null,
    var name: String? = null
)
