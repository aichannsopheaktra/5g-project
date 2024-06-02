package com.example.project001.data

data class CartItem(
    val id: String,
    val customerId: String,
    val productId: String,
    val product: Product,
    val qty: Int,
    val price: Int,
    val amount: Int
)

