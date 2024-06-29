package com.example.project5g.ui

import android.app.AlertDialog
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project5g.R
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.HomeRepository
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CartAdapter
    private lateinit var totalTextView: TextView


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
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewCart)
        progressBar = view.findViewById(R.id.progress)
        totalTextView = view.findViewById(R.id.totalTextView)
        view.findViewById<Button>(R.id.payButton).setOnClickListener {
            showConfirmationDialog()
        }
        initAdapter()
        return view
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Payment")
            .setMessage("Are you sure you want to proceed with the payment?")
            .setPositiveButton("Yes") { _, _ ->
                // User clicked "Yes," handle payment logic here
                viewModel.purchase()
            }
            .setNegativeButton("Cancel") { _, _ ->
                // User clicked "Cancel," do nothing
            }
            .show()
    }
    private fun initAdapter() {
        adapter = CartAdapter(
            ArrayList(),
            totalTextView,
            viewModel
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.fetchCarts()
        // Observe purchase success
        viewModel.purchaseSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                // Reload fragment if purchase was successful
                reloadFragment()
            } else {
                // Handle purchase failure if needed
                // Example: show error message to user
                Toast.makeText(requireContext(), "Purchase failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun reloadFragment() {
        val ft = parentFragmentManager.beginTransaction()
        ft.detach(this).attach(this).commit()
    }

    private fun initViewModel() {
        viewModel.cartData.observe(viewLifecycleOwner, Observer { cartItems ->
            recyclerView.visibility = View.VISIBLE
            if (cartItems != null) {
                adapter.setCartItems(ArrayList(cartItems))
            } else {
                adapter.setCartItems(ArrayList()) // Handle null case appropriately
            }
            progressBar.visibility = View.GONE
        })
    }
}
