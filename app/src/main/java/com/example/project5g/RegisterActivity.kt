package com.example.project5g

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.project5g.ui.RegisterEmailFragment
import com.example.project5g.ui.RegisterInfoFragment

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // Implement your registration functionality here

        val loginTextView = findViewById<TextView>(R.id.loginTextView)
        loginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val registerEmailFragment = RegisterEmailFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, registerEmailFragment)
            .commit()
    }
}