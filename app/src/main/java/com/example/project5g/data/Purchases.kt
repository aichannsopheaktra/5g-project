package com.example.project5g.data

data class Purchases(
    val id: String?,
    val customerId: String?,
    val userId: String?,
    val date: String?,
    val total: Double?,
    val saleDescription: List<SaleDescription>?,
    val customer: String?,
    val user: String?
)
