package com.example.project5g.data

data class LoginRequest(
    val phone: String? = null,
    val email: String? = null,
    val username: String? = null,
    val password: String
)
