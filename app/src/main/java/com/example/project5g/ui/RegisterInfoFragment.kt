package com.example.project5g.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
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
import com.example.project5g.data.RegisterRequest
import com.example.project5g.data.HomeRepository
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory
import kotlin.random.Random

class RegisterInfoFragment : Fragment(R.layout.fragment_register_info) {

    private val factory: HomeViewModelFactory by lazy {
        val apiInterface = ApiClient.instance.create(ApiInterface::class.java)
        val repository = HomeRepository(apiInterface, requireContext())
        HomeViewModelFactory(repository)
    }

    private val viewModel: HomeViewModel by viewModels { factory }
    private var isPasswordVisible1 = false
    private var isPasswordVisible2 = false
    private var email: String? = null
    private var phone: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.fetchUsernames()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        email = arguments?.getString("email")
        phone = arguments?.getString("phone")

        super.onViewCreated(view, savedInstanceState)
        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val newPasswordEditText1 = view.findViewById<EditText>(R.id.editTextPassword)
        val newPasswordEditText2 = view.findViewById<EditText>(R.id.editTextConfirmPassword)
        val editTextDoB = view.findViewById<EditText>(R.id.editTextDoB)
        val eyeIcon2 = view.findViewById<ImageView>(R.id.EyeIcon2)
        val eyeIcon3 = view.findViewById<ImageView>(R.id.EyeIcon3)
        val buttonRegister = view.findViewById<Button>(R.id.buttonRegister)

        // Set up click listeners

        editTextDoB.setOnClickListener {
            // Get the current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create and show the DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    // Set the selected date to the EditText
                    val dobMonth = String.format("%02d", selectedMonth + 1) // Ensure two digits with leading zero
                    val dobDay = String.format("%02d", selectedDayOfMonth) // Ensure two digits with leading zero
                    val selectedDate = "$selectedYear-$dobMonth-$dobDay"
                    editTextDoB.setText(selectedDate)
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        eyeIcon2.setOnClickListener {
            isPasswordVisible1 = !isPasswordVisible1
            togglePasswordVisibility(isPasswordVisible1, newPasswordEditText1)
        }

        eyeIcon3.setOnClickListener {
            isPasswordVisible2 = !isPasswordVisible2
            togglePasswordVisibility(isPasswordVisible2, newPasswordEditText2)
        }

        buttonRegister.setOnClickListener {
            val newPassword1 = newPasswordEditText1.text.toString().trim()
            val newPassword2 = newPasswordEditText2.text.toString().trim()
            val name = editTextName.text.toString().trim()
            val dob = editTextDoB.text.toString().trim()
            val phone = phone
            val email = email

            // Check if any field is empty and show a toast message
            when {
                name.isEmpty() -> Toast.makeText(context, "Name is required", Toast.LENGTH_SHORT).show()
                dob.isEmpty() -> Toast.makeText(context, "Date of Birth is required", Toast.LENGTH_SHORT).show()
                newPassword1.isEmpty() -> Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
                newPassword2.isEmpty() -> Toast.makeText(context, "Confirm Password is required", Toast.LENGTH_SHORT).show()
                newPassword1 != newPassword2 -> Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show()
                else -> {
                    val existingUsernames = viewModel.usernameArray.value ?: emptyList()
                    val uniqueUsernames = existingUsernames.toSet()
                    val formattedExistingUsernames = uniqueUsernames.joinToString(", ", "(", ")")

                    // Split the name to get the last name
                    val nameParts = name.split(" ")
                    val lastName = if (nameParts.size > 1) {
                        nameParts.last()
                    } else {
                        nameParts[0] // If there's only one part, use it as the last name
                    }

                    // Generate a unique username using the last name
                    val username = generateUniqueUsername(lastName, formattedExistingUsernames)
                    val registerRequest = RegisterRequest(
                        customerTypeId = "1da84fbe-4da0-41a7-b0cd-1201cafe4aa7",
                        name = name,
                        dob = dob,
                        phone = phone,
                        balance = 0,
                        sales = null,
                        customerType = null,
                        email = email,
                        username = username,
                        password = newPassword1
                    )

                    viewModel.createCustomer(registerRequest)
                }
            }
        }
        // Observe the ViewModel for customer creation success
        viewModel.customerCreationSuccess.observe(viewLifecycleOwner, { success ->
            if (success) {
                // Redirect to login screen
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish() // Optionally finish this activity to remove it from the back stack
                Toast.makeText(context, "Customer created successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to create customer", Toast.LENGTH_SHORT).show()
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
    private fun generateUniqueUsername(lastName: String, formattedExistingUsernames: String): String {
        var username = lastName
        val random = Random.Default
        while (formattedExistingUsernames.contains(username)) {
            val randomNumber = random.nextInt(100, 999) // Generate a random number between 1000 and 9999
            username = "$lastName$randomNumber"
        }
        return username
    }
}
