package com.example.project5g.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.project5g.R
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.HomeRepository
import com.example.project5g.data.PinResponse
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory

class RegisterEmailFragment : Fragment(R.layout.fragment_register_email) {

    private val factory: HomeViewModelFactory by lazy {
        val apiInterface = ApiClient.instance.create(ApiInterface::class.java)
        val repository = HomeRepository(apiInterface, requireContext())
        HomeViewModelFactory(repository)
    }

    private val viewModel: HomeViewModel by viewModels { factory }
    private var pinResponse: PinResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forgot_email, container, false)
        val editTextEmail = view.findViewById<EditText>(R.id.editTextEmail)
        val editTextPin = view.findViewById<EditText>(R.id.editTextPin)
        val buttonSend = view.findViewById<Button>(R.id.buttonSend)
        val phoneTextView = view.findViewById<TextView>(R.id.phoneTextView)
        val buttonContinue = view.findViewById<Button>(R.id.buttonContinue)

        phoneTextView.setOnClickListener {
            // Replace current fragment with PhoneFragment
            val registerPhoneFragment = RegisterPhoneFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, registerPhoneFragment)
                .addToBackStack(null)  // Optional: Adds transaction to back stack
                .commit()
        }
        buttonSend.setOnClickListener {
            val email = editTextEmail.text.toString().trim()

            if (email.isNotEmpty()) {
                // Call ViewModel method to send email and fetch pin code
                viewModel.sendRegisterEmail(email)
            } else {
                // Show a toast message indicating the email field is empty
                Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
        // Observe pinCode LiveData
        viewModel.pinCode.observe(viewLifecycleOwner, Observer { pinResponse ->
            pinResponse?.let {
                this.pinResponse = it // Store the pinResponse for later use
                if (it.status == "Success") {
                    Toast.makeText(requireContext(), it.status, Toast.LENGTH_SHORT).show()
                } else if (it.status == "Error") {
                    Toast.makeText(requireContext(), it.detail, Toast.LENGTH_SHORT).show()
                }
            }
        })

        buttonContinue.setOnClickListener {
            val pinCode = editTextPin.text.toString().trim()
            val responseCode = pinResponse?.code?.toString() ?: ""
            val email = editTextEmail.text.toString().trim()

            if (email.isNotEmpty()) {
                if (pinCode.isNotEmpty()) {
                    if (responseCode == pinCode) {
                        Toast.makeText(requireContext(), "PIN matched", Toast.LENGTH_SHORT).show()
                        val registerInfoFragment = RegisterInfoFragment()
                        val bundle = Bundle().apply {
                            putString("email", email)
                        }
                        registerInfoFragment.arguments = bundle
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, registerInfoFragment)
                            .addToBackStack(null)  // Optional: Adds transaction to back stack
                            .commit()
                    } else {
                        // Show a toast message indicating the PIN does not match
                        Toast.makeText(requireContext(), "PIN does not match", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Show a toast message indicating the PIN field is empty
                    Toast.makeText(requireContext(), "Please enter your PIN", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show a toast message indicating the email field is empty
                Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}

