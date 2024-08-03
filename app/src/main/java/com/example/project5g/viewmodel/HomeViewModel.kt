package com.example.project5g.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project5g.data.CartItem
import com.example.project5g.data.Customer
import com.example.project5g.data.History
import com.example.project5g.data.HomeProduct
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

    private val _customerData = MutableLiveData<Customer>()
    val customerData: LiveData<Customer> get() = _customerData

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    private val _cartData = MutableLiveData<List<CartItem>?>()
    val cartData: LiveData<List<CartItem>?> get() = _cartData

    private val _purchaseSuccess = MutableLiveData<Boolean>()
    val purchaseSuccess: LiveData<Boolean> = _purchaseSuccess

    private val _purchaseData= MutableLiveData<History>()
    val  purchaseData: LiveData<History> get() = _purchaseData

    private val _homeproductData = MutableLiveData<List<HomeProduct>?>()
    val homeproductData: LiveData<List<HomeProduct>?> get()=_homeproductData

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
        homeRepository.getCustomer().enqueue(object : Callback<Customer> {
            override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
                if (response.isSuccessful) {
                    _customerData.value = response.body()
                } else {

                }
            }

            override fun onFailure(call: Call<Customer>, t: Throwable) {

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
                    val cartItems = response.body()
                    _cartData.value = cartItems
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
                _purchaseSuccess.value = true
                fetchCarts()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _purchaseSuccess.value = false
            }
        })
    }

    fun fetchPurchase(){
        homeRepository.getPurchases(object : Callback<History>{

            //            override fun onResponse(call: Call<History>, response: Response<History>) {
//                if(response.isSuccessful){
//                    _purchaseData.value = response.body();
//                    System.out.println("HomeViewModal: Success");
//                    System.out.println("HomeViewModal: Success");
//                    System.out.println("HomeViewModal: Success");
//                    System.out.println("HomeViewModal: Success");
//                    System.out.println(_purchaseData.value);
//                }else{
//                    _purchaseData.value=null
//                }
//            }
//            override fun onFailure(call: Call<History>, t: Throwable) {
//                _purchaseData.value=null
//                System.out.println(t.message);
//                System.out.println("Fail");
//                System.out.println("Fail");
//                System.out.println("Fail");
//                System.out.println("Fail");
//            }
            override fun onResponse(p0: Call<History>, p1: Response<History>) {
                if(p1.isSuccessful){
                    _purchaseData.value = p1.body();
                    System.out.println("HomeViewModal: Success");
                    System.out.println("HomeViewModal: Success");
                    System.out.println("HomeViewModal: Success");
                    System.out.println("HomeViewModal: Success");
                    System.out.println(_purchaseData.value);
                }else{
                    _purchaseData.value=null
                }
            }

            override fun onFailure(p0: Call<History>, p1: Throwable) {
                _purchaseData.value=null
                System.out.println(p1.message);
                System.out.println("Fail");
                System.out.println("Fail");
                System.out.println("Fail");
                System.out.println("Fail");
            }

        })
    }
    fun fetchHomeProduct(type: String) {
        homeRepository.getHomeProduct(type).enqueue(object: Callback<List<HomeProduct>> {
            override fun onResponse(call: Call<List<HomeProduct>>,response: Response<List<HomeProduct>>
            ) {
                if(response.isSuccessful){
                    _homeproductData.value= response.body();
                    System.out.println("home_product:"+_homeproductData)
                }
            }
            override fun onFailure(call: Call<List<HomeProduct>>, t: Throwable) {
                System.out.println(t);
                System.out.println("Fetch Fail");
            }

        })
    }


}
