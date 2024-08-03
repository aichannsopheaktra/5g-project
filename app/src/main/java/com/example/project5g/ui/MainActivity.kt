package com.example.project5g.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.project5g.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var newPro: TextView
    private lateinit var disPro: TextView
    private  lateinit var underline1: androidx.appcompat.widget.Toolbar
    private  lateinit var underline2: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Check if the authentication token exists in SharedPreferences
        val authToken = sharedPreferences.getString("AUTH_TOKEN", null)
        if (authToken != null) {
            Log.d("MainActivity", "Received auth token: $authToken")
            startMain()
        } else {
            Log.e("MainActivity", "No auth token received")
            // Redirect to LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish() // Finish MainActivity to prevent going back
        }
    }

    private fun startMain() {
        bottomNav = findViewById(R.id.bottom_nav)
        toolbar = findViewById(R.id.toolbar)
        constraintLayout =findViewById(R.id.newdiscounntConstraint)
        newPro= findViewById(R.id.new_pro)
        disPro =findViewById(R.id.dis_pro)
        underline1=findViewById(R.id.u1)
        underline2=findViewById(R.id.u2)
        // Set the initial fragment and selected item
        replaceFragment(HomeFragment::class.java)
        bottomNav.selectedItemId = R.id.navigation_home

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment::class.java)
                    constraintLayout.isVisible=true
                    underline1.isVisible=true
                    underline2.isVisible=false
                    true
                }
                R.id.navigation_product -> {
                    replaceFragment(ProductFragment::class.java)
                    constraintLayout.isVisible=false
                    true
                }
                R.id.navigation_cart -> {
                    replaceFragment(CartFragment::class.java)
                    constraintLayout.isVisible=false
                    true
                }
                R.id.navigation_history -> {
                    replaceFragment(HistoryFragment::class.java)
                    constraintLayout.isVisible=false
                    true
                }
                R.id.navigation_account -> {
                    replaceFragment(AccountFragment::class.java)
                    constraintLayout.isVisible=false
                    true
                }
                else -> false
            }
        }

        newPro.setOnClickListener {
            replaceFragment(HomeFragment::class.java)
            underline1.isVisible=true
            underline2.isVisible=false
//            newPro.setPaintFlags(newPro.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        }
        disPro.setOnClickListener {
            replaceFragment(DiscountFragment::class.java)
            underline1.isVisible=false
            underline2.isVisible=true
//            disPro.setPaintFlags(disPro.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        }
    }
//    fun TextView.underline() {
//        paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
//    }
//    tv_resend_otp.underline()
    private fun replaceFragment(fragmentClass: Class<out Fragment>) {
        val fragment = fragmentClass.newInstance()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.commit()

        // Assuming you've already obtained the reference to the toolbar
        val pageTitle = toolbar.findViewById<TextView>(R.id.pageTitle)

        // Update toolbar text based on fragment
        pageTitle.text = when (fragment) {
            is HomeFragment -> ""
            is ProductFragment -> "Product"
            is CartFragment -> "Buy"
            is HistoryFragment -> "Receipt"
            is AccountFragment -> "User"
            else -> ""
        }
    }

}
