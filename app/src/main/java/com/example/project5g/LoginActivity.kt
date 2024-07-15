package com.example.project5g

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.HomeRepository
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val apiInterface = ApiClient.instance.create(ApiInterface::class.java)
        val homeRepository = HomeRepository(apiInterface, this)
        val viewModelFactory = HomeViewModelFactory(homeRepository)

        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val identifier = findViewById<EditText>(R.id.identifierEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()

            // Check if fields are empty
            if (identifier.isEmpty() || password.isEmpty()) {
                // Show toast message for empty fields
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proceed with login attempt
            homeViewModel.loginUser(identifier, password)

            homeViewModel.loginResponse.observe(this, Observer { loginResponse ->
                if (loginResponse != null) {
                    // Login successful
                    saveAuthToken(loginResponse.token)
                    navigateToMainActivity()

                    // Show success message
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    // Login failed
                    Log.e("LoginActivity", "Login failed")

                    // Show error message
                    Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val forgotPasswordTextView = findViewById<TextView>(R.id.forgotPasswordTextView)
        forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        val registerTextView = findViewById<TextView>(R.id.registerTextView)
        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val eyeIcon = findViewById<ImageView>(R.id.EyeIcon)
        eyeIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT
            } else {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }
    }

    private fun saveAuthToken(token: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("AUTH_TOKEN", token)
            putLong("TOKEN_TIMESTAMP", Calendar.getInstance().timeInMillis)
            apply()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
