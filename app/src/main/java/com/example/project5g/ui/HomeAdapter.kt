package com.example.project5g.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project5g.R
import com.example.project5g.data.Product
import com.example.project5g.data.Purchases
import com.example.project5g.viewmodel.HomeViewModel

class HomeAdapter(private val newproList: ArrayList<Product>,
                  private val viewModel: HomeViewModel): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

//    private val quantitiesMap: MutableMap<String, Int> = mutableMapOf()
    private  var purchasesData: ArrayList<Product> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history,parent,false)
        return HomeAdapter.HomeViewHolder(view, viewModel, this)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
    fun setPurchase(setPro: ArrayList<Product>) {
//        purchasesData = setPurchase;
        newproList.addAll(setPro);
        notifyDataSetChanged()}
    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    class HomeViewHolder(
        itemView: View,
        private val viewModel: HomeViewModel,
        private val adapter: HomeAdapter,
    ): RecyclerView.ViewHolder(itemView) {
//        private val purchasesDate: TextView = itemView.findViewById(R.id.txt_his_date);
//        private val purchaseAmount: TextView = itemView.findViewById(R.id.txt_his_amount);
        fun bind(purchases: Purchases) {
//            val saleDescription = purchases.saleDescription;
//            purchasesDate.text=purchases.date.toString();
//            purchaseAmount.text="$"+purchases.total.toString();

            itemView.findViewById<ImageButton>(R.id.btn_history_detail).setOnClickListener {

            }
        }
    }

}