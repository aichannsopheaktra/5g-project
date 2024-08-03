package com.example.project5g.data

data class Customer(
    val id: String,
    val customerTypeId: String,
    val name: String,
    val dob: String,
    val phone: String,
    val balance: Double,
    val sales: Any? = null,
    val customerType: Any? = null,
    val email: String? = null,
    val username: String? = null,
    var password: String,
    val carts: CartItem? = null
)

