package com.example.project5g.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.project5g.R
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.HomeRepository
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val apiInterface = ApiClient.instance.create(ApiInterface::class.java)
        val homeRepository = HomeRepository(apiInterface, this)
        val viewModelFactory = HomeViewModelFactory(homeRepository)

        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.usernameEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()

            homeViewModel.loginUser(username, password)

            homeViewModel.loginResponse.observe(this, Observer { loginResponse ->
                if (loginResponse != null) {
                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("AUTH_TOKEN", loginResponse.token)
                        apply()
                    }
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("AUTH_TOKEN", loginResponse.token)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("LoginActivity", "Login failed")
                }
            })
        }
    }
}
