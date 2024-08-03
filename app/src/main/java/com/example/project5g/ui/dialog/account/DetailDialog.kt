package com.example.project5g.ui.dialog.account

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.project5g.MainActivity
import com.example.project5g.R
import com.example.project5g.data.Customer
import com.example.project5g.ui.AccountFragment

class DetailDialog(
    private val context: Context,
    private val customer: Customer,
    private val onCustomerUpdated: (Customer) -> Unit
) {

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_account_detail, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val editTextPhone = dialogView.findViewById<EditText>(R.id.editTextPhone)
        val datePickerDOB = dialogView.findViewById<DatePicker>(R.id.datePickerDOB)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)
        val buttonExit = dialogView.findViewById<Button>(R.id.buttonExit) // Add exit button

        // Set current data to EditText fields
        editTextName.setText(customer.name)
        editTextEmail.setText(customer.email)
        editTextPhone.setText(customer.phone)

        // Set DOB in the DatePicker if available
        customer.dob?.let { dob ->
            val dobParts = dob.split("-")
            if (dobParts.size == 3) {
                val year = dobParts[0].toInt()
                val month = dobParts[1].toInt() - 1 // Month is zero-based
                val day = dobParts[2].toInt()
                datePickerDOB.init(year, month, day, null)
            }
        }

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

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val phone = editTextPhone.text.toString()

            val dobYear = datePickerDOB.year
            val dobMonth = String.format("%02d", datePickerDOB.month + 1) // Ensure two digits with leading zero
            val dobDay = String.format("%02d", datePickerDOB.dayOfMonth) // Ensure two digits with leading zero
            val dob = "$dobYear-$dobMonth-$dobDay"

            // Check if any data has changed
            if (name == customer.name && email == customer.email && phone == customer.phone && dob == customer.dob) {
                // Display toast message indicating no changes
                Toast.makeText(context, "Nothing has changed", Toast.LENGTH_SHORT).show()
            } else {
                // Update customer data
                val updatedCustomer = customer.copy(name = name, email = email, phone = phone, dob = dob)

                // Trigger the callback with updated customer data
                onCustomerUpdated(updatedCustomer)

                // Display success toast message
                Toast.makeText(context, "Customer details updated successfully", Toast.LENGTH_SHORT).show()

                // Call replaceFragment from MainActivity
                if (context is MainActivity) {
                    context.replaceFragment(AccountFragment::class.java)
                }

                // Dismiss the dialog
                dialog.dismiss()
            }
        }

        buttonExit.setOnClickListener {
            // Simply dismiss the dialog
            dialog.dismiss()
        }
        dialog.show()
    }
}
