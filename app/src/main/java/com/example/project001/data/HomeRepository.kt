package com.example.project001.data

import android.content.Context
import android.content.SharedPreferences
import com.example.project001.api.ApiInterface
import com.example.project001.data.CartItem
import com.example.project001.data.LoginRequest
import com.example.project001.data.LoginResponse
import retrofit2.Call
import retrofit2.Callback

class HomeRepository(private val apiInterface: ApiInterface, context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private var token: String = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""

    fun getCustomer() = apiInterface.getCustomer()

    fun getProduct() = apiInterface.getProduct()

    fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> = apiInterface.loginUser(loginRequest)

    fun getCart(callback: Callback<List<CartItem>>) {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token" // Add the token to the Authorization header
        apiInterface.getCart(headers).enqueue(callback)
    }

    fun addToCart(productId: String): Call<Void> {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        return apiInterface.cart("CustomerAPI/add-cart?ProductId=$productId", headers)
    }

    fun reduceCart(productId: String): Call<Void> {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        return apiInterface.cart("CustomerAPI/reduce-cart?ProductId=$productId", headers)
    }
}
