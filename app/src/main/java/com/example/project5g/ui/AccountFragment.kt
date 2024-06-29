package com.example.project5g.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.project5g.LoginActivity
import com.example.project5g.R
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.Customer
import com.example.project5g.data.HomeRepository
import com.example.project5g.ui.dialog.account.BalanceDialog
import com.example.project5g.ui.dialog.account.ChangePassDialog
import com.example.project5g.ui.dialog.account.DetailDialog
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory

class AccountFragment : Fragment(R.layout.fragment_account) {
    private lateinit var logoutButton: Button
    private lateinit var detailButton: Button
    private lateinit var customerName: TextView
    private lateinit var customerEmail: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var checkBalanceCard: CardView
    private lateinit var changePasswordCard: CardView

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
        checkBalanceCard = view.findViewById(R.id.checkBalanceCard)
        changePasswordCard = view.findViewById(R.id.changePasswordCard)


        logoutButton.setOnClickListener {
            // Clear the authentication token from SharedPreferences
            val sharedPreferences =
                requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("AUTH_TOKEN").apply()

            // Redirect to LoginActivity
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish() // Finish the current activity to prevent going back to it
        }
        detailButton.setOnClickListener {
            viewModel.customerData.value?.let { customer ->
                DetailDialog(requireContext(), customer).show()
            }
        }
        checkBalanceCard.setOnClickListener {
            viewModel.customerData.value?.let { customer ->
                BalanceDialog(requireContext(), customer).show()
            }
        }
        changePasswordCard.setOnClickListener {
            viewModel.customerData.value?.let { customer ->
                ChangePassDialog(requireContext(), customer).show()
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

}
