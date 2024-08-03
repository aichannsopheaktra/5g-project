package com.example.project5g.ui.dialog.account

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.project5g.MainActivity
import com.example.project5g.R
import com.example.project5g.data.Customer
import com.example.project5g.ui.AccountFragment

class ChangePassDialog(
    private val context: Context,
    private val customer: Customer,
    private val onCustomerUpdated: (Customer) -> Unit
) {

    private var isPasswordVisible1 = false
    private var isPasswordVisible2 = false
    private var isPasswordVisible3 = false

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_account_change_pass, null)
        val oldPasswordEditText = dialogView.findViewById<EditText>(R.id.oldPasswordEditText)
        val newPasswordEditText1 = dialogView.findViewById<EditText>(R.id.newPasswordEditText1)
        val newPasswordEditText2 = dialogView.findViewById<EditText>(R.id.newPasswordEditText2)
        val eyeIcon1 = dialogView.findViewById<ImageView>(R.id.eyeIcon1)
        val eyeIcon2 = dialogView.findViewById<ImageView>(R.id.eyeIcon2)
        val eyeIcon3 = dialogView.findViewById<ImageView>(R.id.eyeIcon3)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)
        val buttonExit = dialogView.findViewById<Button>(R.id.buttonExit)
        // Create the dialog
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

        // Set click listeners for eye icons to toggle password visibility
        eyeIcon1.setOnClickListener {
            isPasswordVisible1 = !isPasswordVisible1
            togglePasswordVisibility(isPasswordVisible1, oldPasswordEditText)
        }

        eyeIcon2.setOnClickListener {
            isPasswordVisible2 = !isPasswordVisible2
            togglePasswordVisibility(isPasswordVisible2, newPasswordEditText1)
        }

        eyeIcon3.setOnClickListener {
            isPasswordVisible3 = !isPasswordVisible3
            togglePasswordVisibility(isPasswordVisible3, newPasswordEditText2)
        }

        // Set click listener for Save button
        buttonSave.setOnClickListener {
            // Handle saving new passwords
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword1 = newPasswordEditText1.text.toString()
            val newPassword2 = newPasswordEditText2.text.toString()

            // Validate if any field is empty
            if (oldPassword.isBlank() || newPassword1.isBlank() || newPassword2.isBlank()) {
                // Show error message that fields cannot be empty
                Toast.makeText(context, "Please fill in all password fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (oldPassword == customer.password) {
                if (newPassword1 == newPassword2) {
                    // Update customer's password (in your actual app, you would perform the update here)
                    // Update customer's password
                    customer.password = newPassword1

                    // Trigger the callback with updated customer data
                    onCustomerUpdated(customer)

                    if (context is MainActivity) {
                        context.replaceFragment(AccountFragment::class.java)
                    }

                    // Show success message
                    Toast.makeText(context, "Password changed successfully!", Toast.LENGTH_SHORT).show()

                    // Dismiss the dialog
                    dialog.dismiss()
                } else {
                    // Show error message that new passwords do not match
                    Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show error message that old password is incorrect
                Toast.makeText(context, "Incorrect old password", Toast.LENGTH_SHORT).show()
            }
        }


        // Set click listener for Exit button
        buttonExit.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    private fun togglePasswordVisibility(isVisible: Boolean, editText: EditText) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        editText.setSelection(editText.text.length)
    }
}
