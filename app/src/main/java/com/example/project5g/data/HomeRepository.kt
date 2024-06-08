package com.example.project5g.data

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.example.project5g.api.ApiInterface
import com.example.project5g.ui.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import java.util.Calendar
import java.util.concurrent.TimeUnit

class HomeRepository(private val apiInterface: ApiInterface, private val context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private var token: String = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""
    private var tokenTimestamp: Long = sharedPreferences.getLong("TOKEN_TIMESTAMP", 0)

    private fun isTokenValid(tokenTimestamp: Long): Boolean {
        val currentTime = Calendar.getInstance().timeInMillis
        val difference = currentTime - tokenTimestamp
        Log.e("HomeRepository", "currentTime: $currentTime, tokenTimestamp: $tokenTimestamp, difference: $difference")
        return difference < TimeUnit.HOURS.toMillis(1)
    }

    private fun checkTokenValidity() {
        if (!isTokenValid(tokenTimestamp)) {
            Log.e("HomeRepository", "Token is invalid or expired")
            sharedPreferences.edit().remove("AUTH_TOKEN").apply()
            // Redirect to LoginActivity
            val loginIntent = Intent(context, LoginActivity::class.java)
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(loginIntent)
        }
    }

    fun getCustomer(): Call<List<Customer>> {
        checkTokenValidity()
        return apiInterface.getCustomer()
    }

    fun getProduct(): Call<List<Product>> {
        checkTokenValidity()
        return apiInterface.getProduct()
    }

    fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> {
        return apiInterface.loginUser(loginRequest)
    }

    fun getCart(callback: Callback<List<CartItem>>) {
        checkTokenValidity()
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token" // Add the token to the Authorization header
        apiInterface.getCart(headers).enqueue(callback)
    }

    fun addToCart(productId: String): Call<Void> {
        checkTokenValidity()
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        return apiInterface.cart("CustomerAPI/add-cart?ProductId=$productId", headers)
    }

    fun reduceCart(productId: String): Call<Void> {
        checkTokenValidity()
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        return apiInterface.cart("CustomerAPI/reduce-cart?ProductId=$productId", headers)
    }

    fun getPurchases(callback: Callback<List<Purchases>>){
        val headers=HashMap<String,String>()
        headers["Authorization"] = "Bearer $token"
        apiInterface.getPurchases(headers).enqueue(callback)
    fun purchase(): Call<Void> {
        checkTokenValidity()
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        return apiInterface.purchase(headers)
    }
}
