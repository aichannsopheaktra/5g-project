package com.example.project5g.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.project5g.R
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.Customer
import com.example.project5g.data.HomeRepository
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory

class AccountFragment : Fragment(R.layout.fragment_account) {
    private lateinit var logoutButton: Button
    private lateinit var detailButton: Button
    private lateinit var customerName: TextView
    private lateinit var customerEmail: TextView
    private lateinit var progressBar: ProgressBar

    private val factory: HomeViewModelFactory by lazy {
        val apiInterface = ApiClient.instance.create(ApiInterface::class.java)
        val repository = HomeRepository(apiInterface, requireContext())
        HomeViewModelFactory(repository)
    }

    private val viewModel: HomeViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        customerName = view.findViewById(R.id.customerName)
        customerEmail = view.findViewById(R.id.customerEmail)
        logoutButton = view.findViewById(R.id.logoutButton)
        detailButton = view.findViewById(R.id.detailButton)
        progressBar = view.findViewById(R.id.progress)


        logoutButton.setOnClickListener {
            // Clear the authentication token from SharedPreferences
            val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("AUTH_TOKEN").apply()

            // Redirect to LoginActivity
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish() // Finish the current activity to prevent going back to it
        }
        detailButton.setOnClickListener {
            viewModel.customerData.value?.let { customer ->
                showDetailDialog(customer)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.customerData.observe(viewLifecycleOwner, Observer { customer ->
            if (customer != null) {
                displayCustomerData(customer)
            } else {
//                displayDefaultCustomerData()
            }
            progressBar.visibility = View.GONE
        })

        viewModel.fetchCustomer()
    }

    private fun displayCustomerData(customer: Customer) {
        customerName.text = customer.name
        customerEmail.text = customer.email
    }

//    private fun displayDefaultCustomerData() {
//        customerName.text = getString(R.string.default_customer_name)
//        customerEmail.text = getString(R.string.default_customer_email)
//    }

    private fun showDetailDialog(customer: Customer) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_account_detail, null)
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

        val dialog = AlertDialog.Builder(requireContext())
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
            customerName.text = name
            customerEmail.text = email

            // Dismiss the dialog
            dialog.dismiss()
        }

        dialog.show()
    }
}
