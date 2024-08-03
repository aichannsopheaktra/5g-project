package com.example.project5g.ui.dialog.account

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.project5g.R
import com.example.project5g.data.Customer

class BalanceDialog(private val context: Context, private val customer: Customer) {

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_account_balance, null)
        val customerName = dialogView.findViewById<TextView>(R.id.customerName)
        val customerUsername = dialogView.findViewById<TextView>(R.id.customerUsername)
        val customerBalance = dialogView.findViewById<TextView>(R.id.customerBalance)
        val buttonExit = dialogView.findViewById<Button>(R.id.buttonExit)

        // Set current data to EditText fields
        customerName.setText(customer.name)
        customerUsername.setText(customer.username)
        val formattedBalance = String.format("%.2f", customer.balance)
        customerBalance.text = "$ $formattedBalance"


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

        buttonExit.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
