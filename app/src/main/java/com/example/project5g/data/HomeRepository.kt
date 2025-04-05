package com.example.project5g.data

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.example.project5g.api.ApiInterface
import com.example.project5g.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Url
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

    fun getCustomers(): Call<Customer> {
        checkTokenValidity()
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token" // Add the token to the Authorization header
        return apiInterface.getCustomers(headers)
    }

    fun getUsernames(): Call<List<String>> {
        return apiInterface.getUsernames()
    }

    fun updateCustomer(customer: Customer): Call<Customer> {
        checkTokenValidity()
        return apiInterface.updateCustomer(customer.id, customer)
    }

    fun updatePassword(id: String, password: String): Call<Void> {
        checkTokenValidity()
        return apiInterface.updatePassword(id, password)
    }

    fun getProduct(): Call<List<Product>> {
        checkTokenValidity()
        return apiInterface.getProducts()
    }

    fun getCategories(): Call<List<Categories>> {
        checkTokenValidity()
        return apiInterface.getCategories()
    }

    fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> {
        return apiInterface.loginUser(loginRequest)
    }

    fun createCustomer(registerRequest: RegisterRequest): Call<Void> {
        return apiInterface.createCustomer(registerRequest)
    }

    fun sendForgotPasswordEmail(email: String): Call<PinResponse> {
        val endpoint = "CustomerAPI/forgot?email=$email"
        return apiInterface.sendEmail(endpoint)
    }

    fun sendForgotPasswordPhone(phone: String): Call<PinResponse> {
        val endpoint = "CustomerAPI/forgot?phone=$phone"
        return apiInterface.sendPhone(endpoint)
    }
    fun sendRegisterEmail(email: String): Call<PinResponse> {
        val endpoint = "CustomerAPI/register?email=$email"
        return apiInterface.sendEmail(endpoint)
    }

    fun sendRegisterPhone(phone: String): Call<PinResponse> {
        val endpoint = "CustomerAPI/register?phone=$phone"
        return apiInterface.sendPhone(endpoint)
    }

    fun getCart(callback: Callback<List<CartItem>>) {
        checkTokenValidity()
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
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

    fun purchase(balance: Boolean): Call<Void> {
        checkTokenValidity()
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        return apiInterface.purchase("CustomerAPI/purchase?balance=$balance", headers)
    }

    fun getPurchases(pageNumber: Int, callback: Callback<History>) {
        checkTokenValidity()
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        apiInterface.getPurchases(headers, pageNumber).enqueue(callback)
    }



    fun getHomeProduct(type: String): Call<List<HomeProduct>> {
        checkTokenValidity()
        val headers = HashMap<String, String>()
        var f = "CustomerAPI/products?type=$type";
        return apiInterface.getHomeProduct(f)
    }

    fun scanToPay(id: String): Call<Void> {
        checkTokenValidity()
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        return apiInterface.scanToPay("CustomerAPI/ValidatePayment/$id", headers)
    }
}
