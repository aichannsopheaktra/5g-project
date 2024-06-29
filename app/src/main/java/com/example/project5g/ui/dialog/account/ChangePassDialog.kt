package com.example.project5g.ui.dialog.account

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import com.example.project5g.R
import com.example.project5g.data.Customer

class ChangePassDialog(private val context: Context, private val customer: Customer) {

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_account_change_pass, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val editTextPhone = dialogView.findViewById<EditText>(R.id.editTextPhone)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)

        // Set current data to EditText fields
        editTextName.setText(customer.name)
        editTextEmail.setText(customer.email)
        editTextPhone.setText(customer.phone)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Change Password")
            .create()

        buttonSave.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
