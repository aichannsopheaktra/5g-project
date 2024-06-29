package com.example.project5g.ui.dialog.account

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import com.example.project5g.R
import com.example.project5g.data.Customer

class DetailDialog(private val context: Context, private val customer: Customer) {

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_account_detail, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val editTextPhone = dialogView.findViewById<EditText>(R.id.editTextPhone)
        val datePickerDOB = dialogView.findViewById<DatePicker>(R.id.datePickerDOB)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)

        // Set current data to EditText fields
        editTextName.setText(customer.name)
        editTextEmail.setText(customer.email)
        editTextPhone.setText(customer.phone)

        customer.dob?.let { dob ->
            // Parse DOB string into year, month, and day components
            val dobParts = dob.split("-")
            if (dobParts.size == 3) {
                val year = dobParts[0].toInt()
                val month = dobParts[1].toInt() - 1 // Month in DatePicker is zero-based
                val day = dobParts[2].toInt()

                // Set DOB in the DatePicker
                datePickerDOB.init(year, month, day, null)
            }
        }

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Customer Details")
            .create()

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val phone = editTextPhone.text.toString()

            // Extracting the selected date from DatePicker
            val dobYear = datePickerDOB.year
            val dobMonth = datePickerDOB.month + 1 // adding 1 because DatePicker months are 0-based
            val dobDay = datePickerDOB.dayOfMonth
            val dob = "$dobDay/$dobMonth/$dobYear"

            // Update displayed customer data
            // customerName.text = name  // You can't directly update the UI from here.
            // customerEmail.text = email  // You can't directly update the UI from here.

            // Here you would ideally use a callback to update the customer data in the Fragment

            // Dismiss the dialog
            dialog.dismiss()
        }

        dialog.show()
    }
}
