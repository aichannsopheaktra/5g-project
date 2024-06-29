package com.example.project5g.data

data class Customer(
    val id: String,
    val customerTypeId: String,
    val name: String,
    val dob: String,
    val phone: String,
    val balance: Int,
    val sales: Any,
    val customerType: Any,
    val email: String,
    val username: String,
    val password: String,
    val carts: Any
)
