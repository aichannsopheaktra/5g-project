package com.example.project5g.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project5g.R
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.HomeProduct
import com.example.project5g.data.HomeRepository
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var progressBar: ProgressBar

    private val factory: HomeViewModelFactory by lazy {
        val apiInterface = ApiClient.instance.create(ApiInterface::class.java)
        val repository = HomeRepository(apiInterface, requireContext())
        HomeViewModelFactory(repository)
    }
    private val viewModel: HomeViewModel by viewModels { factory }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        recyclerView =view.findViewById(R.id.recyclerViewHomePro);
        progressBar = view.findViewById(R.id.progressHomePro)

        initAdpater();
        return view
    }
    private fun initAdpater(){
        adapter = HomeAdapter(ArrayList(),viewModel);
        recyclerView.layoutManager = LinearLayoutManager(requireContext());
        recyclerView.adapter= adapter;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchHomeProduct("new")
        initViewModel();
    }
    private fun initViewModel(){
        viewModel.homeproductData.observe(viewLifecycleOwner, Observer { proItem ->

            // Check if proItem is null or empty
            if (proItem.isNullOrEmpty()) {
                adapter.setProduct(ArrayList()) // Set empty list to adapter if no data
            } else {
                adapter.setProduct(proItem as ArrayList<HomeProduct>) // Cast to ArrayList if necessary
            }
            recyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

        })
    }
}
