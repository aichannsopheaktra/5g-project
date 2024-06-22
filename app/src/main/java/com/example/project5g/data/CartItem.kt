package com.example.project5g.data

data class CartItem(
    val id: String,
    val customerId: String,
    val productId: String,
    val product: Product,
    val qty: Int,
    val price: Float,
    val amount: Float 
)
