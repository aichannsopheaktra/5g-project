package com.example.project5g.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project5g.R
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.HomeRepository
import com.example.project5g.data.Customer
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory

class AccountFragment : Fragment(R.layout.fragment_account) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AccountAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var logoutButton: Button

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

        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        logoutButton = view.findViewById(R.id.logoutButton)

        initAdapter()

        logoutButton.setOnClickListener {
            // Clear the authentication token from SharedPreferences
            val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("AUTH_TOKEN").apply()

            // Redirect to LoginActivity
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish() // Finish the current activity to prevent going back to it
        }

        return view
    }

    private fun initAdapter() {
        adapter = AccountAdapter(ArrayList()) // Pass an empty list initially
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.customerData.observe(viewLifecycleOwner, Observer { customers ->
            recyclerView.visibility = View.VISIBLE
            adapter.setCustomerData(customers as ArrayList<Customer>)
            progressBar.visibility = View.GONE
        })

        viewModel.fetchCustomers()
    }
}
