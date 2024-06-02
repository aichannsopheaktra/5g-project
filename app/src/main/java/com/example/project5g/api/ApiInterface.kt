package com.example.project5g.api

import com.example.project5g.data.CartItem
import com.example.project5g.data.Customer
import com.example.project5g.data.LoginRequest
import com.example.project5g.data.LoginResponse
import com.example.project5g.data.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiInterface {
    @GET("CustomerAPI")
    fun getCustomer(): Call<List<Customer>>

    @GET("CustomerAPI/products?name=&categoryId")
    fun getProduct(): Call<List<Product>>

    @POST("CustomerAPI/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("CustomerAPI/carts")
    fun getCart(@HeaderMap headers: Map<String, String>): Call<List<CartItem>>

    @GET
    fun cart(@Url url: String, @HeaderMap headers: Map<String, String>): Call<Void>

}
