package com.example.project5g.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project5g.data.CartItem
import com.example.project5g.data.Customer
import com.example.project5g.data.HomeRepository
import com.example.project5g.data.LoginRequest
import com.example.project5g.data.LoginResponse
import com.example.project5g.data.Product
import com.example.project5g.data.Purchases
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _productData = MutableLiveData<List<Product>>()
    val productData: LiveData<List<Product>> get() = _productData

    private val _customerData = MutableLiveData<List<Customer>>()
    val customerData: LiveData<List<Customer>> get() = _customerData

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    private val _cartData = MutableLiveData<List<CartItem>?>()
    val cartData: LiveData<List<CartItem>?> get() = _cartData

    private val _purchaseData= MutableLiveData<List<Purchases>?>()
    val  purchaseData: LiveData<List<Purchases>?> get() = _purchaseData

    fun fetchProduct() {
        homeRepository.getProduct().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    _productData.value = response.body()
                } else {
                    _productData.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                _productData.value = emptyList()
            }
        })
    }

    fun fetchCustomer() {
        homeRepository.getCustomer().enqueue(object : Callback<List<Customer>> {
            override fun onResponse(call: Call<List<Customer>>, response: Response<List<Customer>>) {
                if (response.isSuccessful) {
                    _customerData.value = response.body()
                } else {
                    _customerData.value = emptyList() // Handle the case where the response is not successful
                }
            }

            override fun onFailure(call: Call<List<Customer>>, t: Throwable) {
                _customerData.value = emptyList() // Handle failure
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
    fun purchase() {
        homeRepository.purchase().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                // Handle response if needed
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure if needed
            }
        })
    }

    fun fetchPurchase(){
        homeRepository.getPurchases(object : Callback<List<Purchases>>{
            override fun onResponse(call: Call<List<Purchases>>, response: Response<List<Purchases>>) {
                if(response.isSuccessful){
                    _purchaseData.value = response.body();
                    System.out.println(_purchaseData.value);
                }else{
                    _purchaseData.value=null
                }
            }

            override fun onFailure(call: Call<List<Purchases>>, t: Throwable) {
                _purchaseData.value=null
                System.out.println(t.message);
                System.out.println("Fail");
            }

        })
    }


}
