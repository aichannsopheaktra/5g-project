package com.example.project5g.ui.dialog.cart

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import androidx.cardview.widget.CardView
import com.example.project5g.R
import com.example.project5g.viewmodel.HomeViewModel

class CartDialog(private val context: Context, private val viewModel: HomeViewModel) {

    fun show(total: Float) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_cart_payment_method, null)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val payOnBalanceCard = dialogView.findViewById<CardView>(R.id.payOnBalanceCard)
        val payOnDeliveryCard = dialogView.findViewById<CardView>(R.id.payOnDeliveryCard)

        val dialog = Dialog(context, R.style.Dialog)
        dialog.setContentView(dialogView)
        // Set the width and height of the dialog
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(window.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = layoutParams
        }

        payOnBalanceCard.setOnClickListener {
            viewModel.customerData.value?.let { customer ->
                CartConfirmPasswordDialog(context, customer, viewModel, total, paymentType = true).show()
            }
            dialog.dismiss()
        }
        payOnDeliveryCard.setOnClickListener {
            viewModel.customerData.value?.let { customer ->
                CartConfirmPasswordDialog(context, customer, viewModel, total, paymentType = false).show()
            }
            dialog.dismiss()
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
