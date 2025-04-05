package com.example.project5g.api

import com.example.project5g.data.CartItem
import com.example.project5g.data.Categories
import com.example.project5g.data.Customer
import com.example.project5g.data.History
import com.example.project5g.data.HomeProduct
import com.example.project5g.data.LoginRequest
import com.example.project5g.data.LoginResponse
import com.example.project5g.data.PinResponse
import com.example.project5g.data.Product
import com.example.project5g.data.Purchases
import com.example.project5g.data.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {
    @GET("CustomerAPI")
    fun getCustomers(@HeaderMap headers: Map<String, String>): Call<Customer>

    @GET("CustomerAPI/GetUsernames")
    fun getUsernames(): Call<List<String>>

    @PUT("CustomerAPI/{id}")
    fun updateCustomer(@Path("id") id: String, @Body customer: Customer): Call<Customer>

    @PUT("CustomerAPI/password/{id}")
    fun updatePassword(
        @Path("id") userId: String,
        @Query("password") newPassword: String
    ): Call<Void>

    @GET("CustomerAPI/products?categoryId&type=all")
    fun getProducts(): Call<List<Product>>

    @GET("CustomerAPI/categories")
    fun getCategories(): Call<List<Categories>>

    @POST("CustomerAPI/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("CustomerAPI")
    fun createCustomer(@Body customerRequest: RegisterRequest): Call<Void>

    @GET("CustomerAPI/carts")
    fun getCart(@HeaderMap headers: Map<String, String>): Call<List<CartItem>>

    @GET
    fun cart(@Url url: String, @HeaderMap headers: Map<String, String>): Call<Void>

    @POST
    fun purchase(@Url url: String, @HeaderMap headers: Map<String, String>): Call<Void>
    
    @GET("CustomerAPI/purchases")
    fun getPurchases(
        @HeaderMap headers: Map<String, String>,
        @Query("pageNumber") pageNumber: Int
    ): Call<History>

    @GET
    fun sendEmail(@Url email: String): Call<PinResponse>

    @GET
    fun sendPhone(@Url phone: String): Call<PinResponse>

//    testing
    @GET
    fun getHomeProduct(@Url url: String): Call<List<HomeProduct>>

    @GET
    fun scanToPay(@Url url: String, @HeaderMap headers: Map<String, String>): Call<Void>



}
