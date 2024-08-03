package com.example.project5g.ui.dialog.cart

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
import com.example.project5g.R
import com.example.project5g.data.Customer
import com.example.project5g.viewmodel.HomeViewModel

class CartConfirmPasswordDialog(
    private val context: Context,
    private val customer: Customer,
    private val viewModel: HomeViewModel,
    private val total: Float,
    private val paymentType: Boolean

) {
    private var isPasswordVisible = false
    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_password, null)
        val editTextPassword = dialogView.findViewById<EditText>(R.id.editTextPassword)
        val buttonConfirm = dialogView.findViewById<Button>(R.id.buttonConfirm)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)
        val eyeIcon = dialogView.findViewById<ImageView>(R.id.eyeIcon)

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
        eyeIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(isPasswordVisible, editTextPassword)
        }
        buttonConfirm.setOnClickListener {
            val inputPassword = editTextPassword.text.toString()

            when {
                inputPassword.isEmpty() -> {
                    Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                }
                inputPassword == customer.password -> {
                    when {
                        paymentType && total <= customer.balance -> {
                            viewModel.purchase(balance = true)
                            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                        }
                        !paymentType -> {
                            viewModel.purchase(balance = false)
                            Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(context, "Balance Is Not Enough", Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.dismiss()
                }
                else -> {
                    Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
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
