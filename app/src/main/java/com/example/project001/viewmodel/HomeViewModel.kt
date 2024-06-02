package com.example.project001.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project001.data.CartItem
import com.example.project001.data.Customer
import com.example.project001.data.HomeRepository
import com.example.project001.data.LoginRequest
import com.example.project001.data.LoginResponse
import com.example.project001.data.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _productData = MutableLiveData<List<Product>?>()
    val productData: LiveData<List<Product>?> get() = _productData

    private val _customerData = MutableLiveData<List<Customer>?>()
    val customerData: LiveData<List<Customer>?> get() = _customerData

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    private val _cartData = MutableLiveData<List<CartItem>?>()
    val cartData: LiveData<List<CartItem>?> get() = _cartData

    fun fetchProduct() {
        homeRepository.getProduct().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    _productData.value = response.body()
                } else {
                    _productData.value = null
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                _productData.value = null
            }
        })
    }

    fun fetchCustomers() {
        homeRepository.getCustomer().enqueue(object : Callback<List<Customer>> {
            override fun onResponse(call: Call<List<Customer>>, response: Response<List<Customer>>) {
                if (response.isSuccessful) {
                    _customerData.value = response.body()
                } else {
                    _customerData.value = null
                }
            }

            override fun onFailure(call: Call<List<Customer>>, t: Throwable) {
                _customerData.value = null
            }
        })
    }

    fun loginUser(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        homeRepository.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                } else {
                    _loginResponse.value = null
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginResponse.value = null
            }
        })
    }
    fun fetchCarts() {
        homeRepository.getCart(object : Callback<List<CartItem>> {
            override fun onResponse(call: Call<List<CartItem>>, response: Response<List<CartItem>>) {
                if (response.isSuccessful) {
                    _cartData.value = response.body()
                } else {
                    _cartData.value = null
                }
            }

            override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
                _cartData.value = null
            }
        })
    }

    fun addToCart(productId: String) {
        homeRepository.addToCart(productId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                // Handle response if needed
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure if needed
            }
        })
    }

    fun reduceCart(productId: String) {
        homeRepository.reduceCart(productId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                // Handle response if needed
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure if needed
            }
        })
    }

}
