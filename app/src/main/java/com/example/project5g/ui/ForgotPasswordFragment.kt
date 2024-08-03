package com.example.project5g.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.project5g.LoginActivity
import com.example.project5g.R
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.HomeRepository
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private val factory: HomeViewModelFactory by lazy {
        val apiInterface = ApiClient.instance.create(ApiInterface::class.java)
        val repository = HomeRepository(apiInterface, requireContext())
        HomeViewModelFactory(repository)
    }

    private val viewModel: HomeViewModel by viewModels { factory }
    private var isPasswordVisible1 = false
    private var isPasswordVisible2 = false
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userId = arguments?.getString("customerId")

        super.onViewCreated(view, savedInstanceState)
        val newPasswordEditText1 = view.findViewById<EditText>(R.id.NewPasswordEditText1)
        val newPasswordEditText2 = view.findViewById<EditText>(R.id.NewPasswordEditText2)
        val eyeIcon2 = view.findViewById<ImageView>(R.id.EyeIcon2)
        val eyeIcon3 = view.findViewById<ImageView>(R.id.EyeIcon3)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        // Set up click listeners
        eyeIcon2.setOnClickListener {
            isPasswordVisible1 = !isPasswordVisible1
            togglePasswordVisibility(isPasswordVisible1, newPasswordEditText1)
        }

        eyeIcon3.setOnClickListener {
            isPasswordVisible2 = !isPasswordVisible2
            togglePasswordVisibility(isPasswordVisible2, newPasswordEditText2)
        }

        buttonSave.setOnClickListener {
            val newPassword1 = newPasswordEditText1.text.toString().trim()
            val newPassword2 = newPasswordEditText2.text.toString().trim()

            if (newPassword1 == newPassword2) {
                viewModel.updatePassword(userId!!, newPassword1)
            } else {
                // Show error message that new passwords do not match
                Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
        // Observe the ViewModel for password update success
        viewModel.passwordUpdateSuccess.observe(viewLifecycleOwner, { success ->
            if (success) {
                // Redirect to login screen
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish() // Optionally finish this activity to remove it from the back stack
                Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun togglePasswordVisibility(isVisible: Boolean, editText: EditText) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        // Move cursor to the end of the text
        editText.setSelection(editText.text.length)
    }
}
