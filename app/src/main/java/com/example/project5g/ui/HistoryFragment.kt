package com.example.project5g.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project5g.R
import com.example.project5g.api.ApiClient
import com.example.project5g.api.ApiInterface
import com.example.project5g.data.CartItem
import com.example.project5g.data.HomeRepository
import com.example.project5g.data.Purchases
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: HistoryAdapter
//    private lateinit var totalTextView: TextView

    private val factory: HomeViewModelFactory by lazy {
        val apiInterface = ApiClient.instance.create(ApiInterface::class.java)
        val repository = HomeRepository(apiInterface, requireContext())
        HomeViewModelFactory(repository)
    }

    private val viewModel: HomeViewModel by viewModels { factory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_history,container,false)
        recyclerView =view.findViewById(R.id.recyclerViewHistory);
        progressBar= view.findViewById(R.id.progressHistory);
        initAdpater();
        return view


    }
    private fun initAdpater(){
        adapter = HistoryAdapter(ArrayList(),viewModel);
        recyclerView.layoutManager =LinearLayoutManager(requireContext());
        recyclerView.adapter= adapter;

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPurchase();
        initViewModel();


    }
    private fun initViewModel(){
        viewModel.purchaseData.observe(viewLifecycleOwner, Observer { purchaseItem->
            recyclerView.visibility = View.VISIBLE
            System.out.println("sss :"+purchaseItem)

            adapter.setPurchase(purchaseItem as ArrayList<Purchases>)
            progressBar.visibility = View.GONE;
        })
    }

}