package com.example.project5g

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.CaptureManager

class CustomCaptureActivity : AppCompatActivity() {

    private lateinit var QRcodeScanner: DecoratedBarcodeView
    private lateinit var buttonFlash: Button
    private lateinit var buttonExit: Button
    private lateinit var capture: CaptureManager
    private var isFlashOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_capture)

        QRcodeScanner = findViewById(R.id.zxing_QR_code_scanner)
        buttonFlash = findViewById(R.id.button_flash)
        buttonExit = findViewById(R.id.button_exit)

        capture = CaptureManager(this, QRcodeScanner)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.decode()

        buttonFlash.setOnClickListener {
            toggleFlashlight()
        }
        buttonExit.setOnClickListener {
            finish() // Exit the activity
        }
    }

    private fun toggleFlashlight() {
        if (isFlashOn) {
            QRcodeScanner.setTorchOff()
        } else {
            QRcodeScanner.setTorchOn()
        }
        isFlashOn = !isFlashOn
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }
}
