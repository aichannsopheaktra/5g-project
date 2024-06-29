package com.example.project5g.api

import com.example.project5g.data.CartItem
import com.example.project5g.data.Categories
import com.example.project5g.data.Customer
import com.example.project5g.data.HomeProduct
import com.example.project5g.data.LoginRequest
import com.example.project5g.data.LoginResponse
import com.example.project5g.data.Product
import com.example.project5g.data.Purchases
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiInterface {
    @GET("CustomerAPI")
    fun getCustomer(@HeaderMap headers: Map<String, String>): Call<Customer>

    @GET("CustomerAPI/products?categoryId&type=all")
    fun getProduct(): Call<List<Product>>
    @GET("CustomerAPI/categories")
    fun getCategories(): Call<List<Categories>>
    @POST("CustomerAPI/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("CustomerAPI/carts")
    fun getCart(@HeaderMap headers: Map<String, String>): Call<List<CartItem>>

    @GET
    fun cart(@Url url: String, @HeaderMap headers: Map<String, String>): Call<Void>

    @POST("CustomerAPI/purchase")
    fun purchase(@HeaderMap headers: Map<String, String>): Call<Void>
    
    @GET("CustomerAPI/purchases")
    fun getPurchases(@HeaderMap headers: Map<String, String>): Call<List<Purchases>>

//    testing
    @GET
    fun getHomeProduct(@Url url: String): Call<List<HomeProduct>>
//    @GET("CustomerAPI/{product}")
//    fun getHomeProduct(@Path("product") pro : String?, @Query("type") t: String?): Call<List<HomeProduct>>
}
