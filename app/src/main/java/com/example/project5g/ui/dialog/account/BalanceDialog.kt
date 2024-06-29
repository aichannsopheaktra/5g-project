package com.example.project5g.ui.dialog.account

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.example.project5g.R
import com.example.project5g.data.Customer

class BalanceDialog(private val context: Context, private val customer: Customer) {

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_account_balance, null)
        val customerName = dialogView.findViewById<TextView>(R.id.customerName)
        val customerEmail = dialogView.findViewById<TextView>(R.id.customerEmail)
        val customerBalance = dialogView.findViewById<TextView>(R.id.customerBalance)
        val buttonExit = dialogView.findViewById<Button>(R.id.buttonExit)

        // Set current data to EditText fields
        customerName.setText(customer.name)
        customerEmail.setText(customer.email)
        customerBalance.setText(customer.balance.toString())

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Balance")
            .create()

        buttonExit.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
