package com.example.project5g.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project5g.R
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.Categories
import com.example.project5g.data.HomeRepository
import com.example.project5g.data.Product
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory

class ProductFragment : Fragment(R.layout.fragment_product) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var categorySpinner: Spinner
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
        categorySpinner = view.findViewById(R.id.categorySpinner)

        initAdapter()
        initSpinner()

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
        adapter = ProductAdapter(viewModel)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun initSpinner() {
        viewModel.categoriesData.observe(viewLifecycleOwner, Observer<List<Categories>> { categories ->
            // Create a list with a default item at the beginning
            val categoriesWithDefault = mutableListOf("Select Category")
            categoriesWithDefault.addAll(categories.map { it.name })

            // Create an ArrayAdapter with the updated list
            val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoriesWithDefault)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = spinnerAdapter
        })



        // Fetch categories
        viewModel.fetchCategories()
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
