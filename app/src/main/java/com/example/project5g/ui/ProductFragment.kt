package com.example.project5g.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
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

class ProductFragment : Fragment(R.layout.fragment_product) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var categoryContainer: LinearLayout
    private lateinit var rootView: View
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
        rootView = view

        recyclerView = view.findViewById(R.id.rv_home)
        progressBar = view.findViewById(R.id.progress)
        searchView = view.findViewById(R.id.searchView)
        categoryContainer = view.findViewById(R.id.categoryContainer)

        initAdapter()

        rootView.setOnTouchListener { _, _ ->
            searchView.clearFocus()
            false
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.fetchProduct()
        viewModel.fetchCategories()
    }

    private fun initAdapter() {
        adapter = ProductAdapter(viewModel)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun initViewModel() {
        viewModel.productData.observe(viewLifecycleOwner, Observer { products ->
            if (products.isNullOrEmpty()) {
                adapter.setProData(ArrayList()) // Assuming setProData accepts non-null ArrayList
            } else {
                adapter.setProData(ArrayList(products)) // Convert products to ArrayList if necessary
            }

            recyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        })


        viewModel.categoriesData.observe(viewLifecycleOwner, Observer { categories ->
            // Clear existing views in categoryContainer
            categoryContainer.removeAllViews()

            // Inflate the main layout containing the RadioGroup
            val linearLayout =
                layoutInflater.inflate(R.layout.item_category, categoryContainer, false) as LinearLayout
            val radioGroup = linearLayout.findViewById<RadioGroup>(R.id.radioGroup)

            // Create a default RadioButton
            val defaultRadioButton = RadioButton(requireContext()).apply {
                text = "All" // Set your default text here
                textSize = 16f
                background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.radio_button_shadow)
                setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.radio_button_text_color
                    )
                )
                buttonDrawable = null
                id = View.generateViewId() // Generate an ID for the default RadioButton
                isChecked = true // Set this RadioButton as checked by default
            }

            // Setting margin for default RadioButton
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 8, 0, 8) // left, top, right, bottom margins
            }
            defaultRadioButton.layoutParams = params

            defaultRadioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Handle default selection (show all categories or reset filters)
                    adapter.resetCategoryFilter() // Implement this method in your adapter to reset any category filtering
                }
            }

            // Add default RadioButton to the RadioGroup
            radioGroup.addView(defaultRadioButton)

            // Iterate through categories and create RadioButtons
            categories.forEach { category ->
                // Create a new RadioButton for each category
                val radioButton = RadioButton(requireContext()).apply {
                    text = category.name
                    textSize = 16f
                    background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.radio_button_shadow)
                    setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.radio_button_text_color
                        )
                    )
                    buttonDrawable = null
                    id = View.generateViewId()
                }

                // Setting margin for category RadioButtons
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8) // left, top, right, bottom margins
                }
                radioButton.layoutParams = params

                radioButton.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // Handle category selection
                        adapter.filterByCategory(category.id)
                    }
                }

                radioGroup.addView(radioButton)
            }
            categoryContainer.addView(linearLayout)
        })
    }
}
