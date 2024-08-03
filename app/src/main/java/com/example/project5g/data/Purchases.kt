package com.example.project5g.data

data class Purchases(
    var id:String?,
    var customerId :String?,
    var userId:String?,
    var date:String?,
    var total: Double?,
    var saleDescription:List<SaleDescription>?,
    var customer: String?,
    var user:String?,
    var delivered: Boolean
)
