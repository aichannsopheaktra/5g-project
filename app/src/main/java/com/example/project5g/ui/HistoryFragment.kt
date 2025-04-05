package com.example.project5g.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.example.project5g.data.HomeRepository
import com.example.project5g.data.Purchases
import com.example.project5g.data.SaleDescription
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.viewmodel.HomeViewModelFactory
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: HistoryAdapter
    private val factory: HomeViewModelFactory by lazy {
        val apiInterface = ApiClient.instance.create(ApiInterface::class.java)
        val repository = HomeRepository(apiInterface, requireContext())
        HomeViewModelFactory(repository)
    }

    private val viewModel: HomeViewModel by viewModels { factory }
    private var isLoading = false
    private var currentPage = 1
    private var totalPages = 1
    private var isLastPage = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewHistory)
        progressBar = view.findViewById(R.id.progressHistory)
        initAdapter()
        initScrollListener() // Add scroll listener for infinite scroll
        return view
    }

    private fun initAdapter() {
        adapter = HistoryAdapter(requireContext(), ArrayList(), this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun initScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreData()
                    }
                }
            }
        })
    }

    private fun loadMoreData() {
        isLoading = true
        progressBar.visibility = View.VISIBLE
        currentPage++
        viewModel.fetchPurchase(currentPage)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPurchase(currentPage)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.purchaseData.observe(viewLifecycleOwner, Observer { historyResponse ->
            recyclerView.visibility = View.VISIBLE
            historyResponse?.let {
                totalPages = it.pagination.totalPages ?: 1

                // Check if we are on the last page
                isLastPage = currentPage >= totalPages

                // Add new data to adapter
                adapter.setPurchase(it.data as ArrayList<Purchases>)
            }
            progressBar.visibility = View.GONE
            isLoading = false
        })
    }

    fun showDialog(purchases: Purchases) {
        println(purchases);
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.frm_receipt, null)
        val builder = AlertDialog.Builder(requireContext())
            builder.setView(dialogView)

        val col_pro: TextView = dialogView.findViewById(R.id.col_pro)
        val col_price :TextView= dialogView.findViewById(R.id.col_price)
        val col_qty : TextView = dialogView.findViewById(R.id.col_qty)
        val col_amount:TextView=dialogView.findViewById(R.id.col_amount)
        val his_date:TextView=dialogView.findViewById(R.id.his_date)
        val his_amount: TextView=dialogView.findViewById(R.id.txt_his_amount)
        val t_amount : TextView=dialogView.findViewById(R.id.t_amount)
        val deliverry_status : TextView=dialogView.findViewById(R.id.delivery_sts)
        val btn_close: TextView=dialogView.findViewById(R.id.btn_close)

        val parsedDate = LocalDateTime.parse(purchases.date.toString(), DateTimeFormatter.ISO_DATE_TIME)
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("MM.dd.yyyy  HH:mm"))
        var purchasesAmount =purchases.total

        val num_format = DecimalFormat("0.00")
        num_format.roundingMode = RoundingMode.CEILING

        var purchasesDate = formattedDate
        var s_pro: String = "Product Name"
        var s_qty: String="Qty"
        var s_amount: String = "Amount"
        var s_price : String = "Price"
        var DevStatus: String= ""
        var receipt : List<SaleDescription>? = purchases.saleDescription
        if (receipt != null) {
            for (item in receipt) {
                println(item)
                s_pro += "\n" + item.product?.name
                s_price += "\n"+num_format.format(item.price)
                s_amount+="\n"+num_format.format(item.amount)
                s_qty+="\n"+item.qty
            }
        }
        if (purchases.delivered==true){
            DevStatus ="Completed";
        }
        else if(purchases.delivered==false){
            DevStatus ="Waiting";
        }
        else{
            DevStatus= "None";
        }
        his_date.setText(purchasesDate)
        col_pro.setText(s_pro)
        col_qty.setText(s_qty)
        col_amount.setText(s_amount)
        col_price.setText(s_price)
        his_amount.setText("$"+num_format.format(purchasesAmount))
        t_amount.setText("$"+num_format.format(purchasesAmount))
        deliverry_status.setText(DevStatus);
    val dialog = builder.create()

    btn_close.setOnClickListener{dialog.cancel() }
        dialog.show()
    }
}