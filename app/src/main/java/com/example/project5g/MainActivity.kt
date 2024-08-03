package com.example.project5g

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.HomeRepository
import com.example.project5g.ui.AccountFragment
import com.example.project5g.ui.CartFragment
import com.example.project5g.ui.DiscountFragment
import com.example.project5g.ui.HistoryFragment
import com.example.project5g.ui.HomeFragment
import com.example.project5g.ui.ProductFragment
import com.example.project5g.ui.dialog.cart.CartConfirmPasswordDialog
import com.example.project5g.ui.dialog.main.MainConfirmPasswordDialog
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var viewModel: HomeViewModel
    private lateinit var newPro: TextView
    private lateinit var disPro: TextView
    private  lateinit var underline1: androidx.appcompat.widget.Toolbar
    private  lateinit var underline2: androidx.appcompat.widget.Toolbar
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startQRScanner()
        } else {
            Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val apiInterface = ApiClient.instance.create(ApiInterface::class.java)
        val homeRepository = HomeRepository(apiInterface, this)
        val viewModelFactory = HomeViewModelFactory(homeRepository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)


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
        val scanButton = findViewById<ImageButton>(R.id.scanButton)
        scanButton.setOnClickListener {
            viewModel.fetchCustomers()
            checkCameraPermissionAndStartScanner()
        }
    }

    private fun startMain() {
        bottomNav = findViewById(R.id.bottom_nav)
        toolbar = findViewById(R.id.toolbar)
        constraintLayout = findViewById(R.id.newdiscounntConstraint)
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
    fun replaceFragment(fragmentClass: Class<out Fragment>) {
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
            is CartFragment -> "Cart"
            is HistoryFragment -> "History"
            is AccountFragment -> "Account"
            else -> ""
        }
    }

    private fun checkCameraPermissionAndStartScanner() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the camera
                startQRScanner()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // In an educational UI, explain to the user why your app requires this permission for a specific feature to behave as expected.
                Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // You can directly ask for the permission.
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    private fun startQRScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Please scan QR code to pay.")
        integrator.setCameraId(0) // Use a specific camera of the device
        integrator.setBeepEnabled(false) // Enable beep sound
        integrator.setBarcodeImageEnabled(true) // Capture barcode image
        integrator.captureActivity = CustomCaptureActivity::class.java
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val resultContents = result.contents
                viewModel.customerData.value?.let { customer ->
                    MainConfirmPasswordDialog(this, customer, viewModel,resultContents).show()
                }
            } else {
                Toast.makeText(this, "Cancelled scan", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
