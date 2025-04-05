package com.example.project5g.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project5g.data.CartItem
import com.example.project5g.data.Categories
import com.example.project5g.data.Customer
import com.example.project5g.data.History
import com.example.project5g.data.HomeProduct
import com.example.project5g.data.HomeRepository
import com.example.project5g.data.LoginRequest
import com.example.project5g.data.LoginResponse
import com.example.project5g.data.PinResponse
import com.example.project5g.data.Product
import com.example.project5g.data.Purchases
import com.example.project5g.data.RegisterRequest
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _productData = MutableLiveData<List<Product>>()
    val productData: LiveData<List<Product>> get() = _productData

    private val _categoriesData = MutableLiveData<List<Categories>>()
    val categoriesData: LiveData<List<Categories>> get() = _categoriesData

    private val _customerData = MutableLiveData<Customer>()
    val customerData: LiveData<Customer> get() = _customerData

    private val _usernameArray = MutableLiveData<List<String>>()
    val usernameArray: LiveData<List<String>> get() = _usernameArray

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    private val _customerCreationSuccess = MutableLiveData<Boolean>()
    val customerCreationSuccess: LiveData<Boolean> get() = _customerCreationSuccess

    private val _cartData = MutableLiveData<List<CartItem>?>()
    val cartData: LiveData<List<CartItem>?> get() = _cartData

    private val _purchaseSuccess = MutableLiveData<Boolean>()
    val purchaseSuccess: LiveData<Boolean> = _purchaseSuccess

    private val _purchaseData= MutableLiveData<History?>()
    val  purchaseData: MutableLiveData<History?> get() = _purchaseData

    private val _homeproductData = MutableLiveData<List<HomeProduct>?>()
    val homeproductData: LiveData<List<HomeProduct>?> get()=_homeproductData

    private val _pinCode = MutableLiveData<PinResponse>()
    val pinCode: LiveData<PinResponse> get() = _pinCode

    private val _passwordUpdateSuccess = MutableLiveData<Boolean>()
    val passwordUpdateSuccess: LiveData<Boolean> get() = _passwordUpdateSuccess

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
    fun fetchCategories() {
        homeRepository.getCategories().enqueue(object : Callback<List<Categories>> {
            override fun onResponse(call: Call<List<Categories>>, response: Response<List<Categories>>) {
                if (response.isSuccessful) {
                    _categoriesData.value = response.body()
                } else {
                    _categoriesData.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<Categories>>, t: Throwable) {
                _categoriesData.value = emptyList()
            }
        })
    }
    fun fetchCustomers() {
        homeRepository.getCustomers().enqueue(object : Callback<Customer> {
            override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
                if (response.isSuccessful) {
                    _customerData.value = response.body()
                } else {
                    // Handle unsuccessful response, optionally set _customerData.value = null
                }
            }

            override fun onFailure(call: Call<Customer>, t: Throwable) {
                // Handle failure, optionally set _customerData.value = null
            }
        })
    }

    fun fetchUsernames() {
        homeRepository.getUsernames().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    _usernameArray.value = response.body()
                } else {
                    // Handle unsuccessful response, optionally set _customerData.value = null
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                // Handle failure, optionally set _customerData.value = null
            }
        })
    }

    fun updateCustomer(customer: Customer) {
        homeRepository.updateCustomer(customer).enqueue(object : Callback<Customer> {
            override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
                if (response.isSuccessful) {
                    _customerData.value = response.body()
                } else {
                    // Handle unsuccessful response, optionally set _customerData.value = null
                }
            }

            override fun onFailure(call: Call<Customer>, t: Throwable) {
                // Handle failure, optionally set _customerData.value = null
            }
        })
    }

    fun updatePassword(userId: String, newPassword: String) {
        homeRepository.updatePassword(userId, newPassword).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _passwordUpdateSuccess.value = true
                } else {
                    _passwordUpdateSuccess.value = false
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _passwordUpdateSuccess.value = false
            }
        })
    }

    fun loginUser(identifier: String, password: String) {
        val loginRequest = when {
            identifier.contains("@") -> LoginRequest(email = identifier, password = password)
            identifier.all { it.isDigit() } -> LoginRequest(phone = identifier, password = password)
            else -> LoginRequest(username = identifier, password = password)
        }

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

    fun createCustomer(registerRequest: RegisterRequest) {
        homeRepository.createCustomer(registerRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _customerCreationSuccess.value = response.isSuccessful
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _customerCreationSuccess.value = false
            }
        })
    }

    fun sendForgotPasswordEmail(email: String) {
        homeRepository.sendForgotPasswordEmail(email).enqueue(object : Callback<PinResponse> {
            override fun onResponse(call: Call<PinResponse>, response: Response<PinResponse>) {
                if (response.isSuccessful) {
                    _pinCode.value = response.body()
                } else {
                    // Handle unsuccessful response by parsing the error body
                    response.errorBody()?.let { errorBody ->
                        val errorResponse = Gson().fromJson(errorBody.string(), PinResponse::class.java)
                        _pinCode.value = errorResponse
                    }
                }
            }

            override fun onFailure(call: Call<PinResponse>, t: Throwable) {
                // Handle network failure scenario
                val errorResponse = PinResponse("Error", null, null, "Network error: ${t.message}")
                _pinCode.value = errorResponse
            }
        })
    }

    fun sendForgotPasswordPhone(phone: String) {
        homeRepository.sendForgotPasswordPhone(phone).enqueue(object : Callback<PinResponse> {
            override fun onResponse(call: Call<PinResponse>, response: Response<PinResponse>) {
                if (response.isSuccessful) {
                    _pinCode.value = response.body()
                } else {
                    // Handle unsuccessful response by parsing the error body
                    response.errorBody()?.let { errorBody ->
                        val errorResponse = Gson().fromJson(errorBody.string(), PinResponse::class.java)
                        _pinCode.value = errorResponse
                    }
                }
            }

            override fun onFailure(call: Call<PinResponse>, t: Throwable) {
                // Handle network failure scenario
                val errorResponse = PinResponse("Error", null, null, "Network error: ${t.message}")
                _pinCode.value = errorResponse
            }
        })
    }
    fun sendRegisterEmail(email: String) {
        homeRepository.sendRegisterEmail(email).enqueue(object : Callback<PinResponse> {
            override fun onResponse(call: Call<PinResponse>, response: Response<PinResponse>) {
                if (response.isSuccessful) {
                    _pinCode.value = response.body()
                } else {
                    // Handle unsuccessful response by parsing the error body
                    response.errorBody()?.let { errorBody ->
                        val errorResponse = Gson().fromJson(errorBody.string(), PinResponse::class.java)
                        _pinCode.value = errorResponse
                    }
                }
            }

            override fun onFailure(call: Call<PinResponse>, t: Throwable) {
                // Handle network failure scenario
                val errorResponse = PinResponse("Error", null, null, "Network error: ${t.message}")
                _pinCode.value = errorResponse
            }
        })
    }

    fun sendRegisterPhone(phone: String) {
        homeRepository.sendRegisterPhone(phone).enqueue(object : Callback<PinResponse> {
            override fun onResponse(call: Call<PinResponse>, response: Response<PinResponse>) {
                if (response.isSuccessful) {
                    _pinCode.value = response.body()
                } else {
                    // Handle unsuccessful response by parsing the error body
                    response.errorBody()?.let { errorBody ->
                        val errorResponse = Gson().fromJson(errorBody.string(), PinResponse::class.java)
                        _pinCode.value = errorResponse
                    }
                }
            }

            override fun onFailure(call: Call<PinResponse>, t: Throwable) {
                // Handle network failure scenario
                val errorResponse = PinResponse("Error", null, null, "Network error: ${t.message}")
                _pinCode.value = errorResponse
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
    fun purchase(balance: Boolean) {
        homeRepository.purchase(balance).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _purchaseSuccess.value = true
                fetchCarts()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _purchaseSuccess.value = false
            }
        })
    }

    fun fetchPurchase(pageNumber: Int) {
        homeRepository.getPurchases(pageNumber, object : Callback<History> {
            override fun onResponse(p0: Call<History>, p1: Response<History>) {
                if (p1.isSuccessful) {
                    _purchaseData.value = p1.body()
                } else {
                    _purchaseData.value = null
                }
            }

            override fun onFailure(p0: Call<History>, p1: Throwable) {
                _purchaseData.value = null
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
    fun scanToPay(id: String) {
        homeRepository.scanToPay(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                // Handle response if needed
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure if needed
            }
        })
    }

}
