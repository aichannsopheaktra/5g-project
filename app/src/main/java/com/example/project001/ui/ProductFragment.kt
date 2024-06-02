package com.example.project001.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project001.R
import com.example.project001.api.ApiClient
import com.example.project001.api.ApiInterface
import com.example.project001.data.HomeRepository
import com.example.project001.data.Product
import com.example.project001.viewmodel.HomeViewModel
import com.example.project001.viewmodel.HomeViewModelFactory

class ProductFragment : Fragment(R.layout.fragment_product) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var rootView: View // Root view of the fragment layout
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
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        rootView = view // Assign the root view

        recyclerView = view.findViewById(R.id.rv_home)
        progressBar = view.findViewById(R.id.progress)
        searchView = view.findViewById(R.id.searchView)

        initAdapter()

        // Set touch listener to the root view
        rootView.setOnTouchListener { _, _ ->
            // Clear the focus of the search view
            searchView.clearFocus()
            false // Return false to indicate that the listener has not consumed the event
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { adapter.filter(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { adapter.filter(it) }
                return true
            }
        })

        return view
    }

    private fun initAdapter() {
        adapter = ProductAdapter(
            viewModel
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.fetchProduct()
    }

    private fun initViewModel() {
        viewModel.productData.observe(viewLifecycleOwner, Observer { products ->
            recyclerView.visibility = View.VISIBLE
            adapter.setProData(products as ArrayList<Product>)
            progressBar.visibility = View.GONE
        })
    }
}
