package com.example.project5g.data

data class SaleDescription (
    var id: String?,
    var saleId:String?,
    var productId:String?,
    var price:Double?,
    var qty: Int?,
    var amount: Double?,
    var product: Product?
)