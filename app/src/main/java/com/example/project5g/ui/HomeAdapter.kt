package com.example.project5g.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project5g.R
import com.example.project5g.data.HomeProduct
import com.example.project5g.viewmodel.HomeViewModel

class HomeAdapter(private val newproList: ArrayList<HomeProduct>,
                  private val viewModel: HomeViewModel): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

//    private val quantitiesMap: MutableMap<String, Int> = mutableMapOf()
    private  var productData: ArrayList<HomeProduct> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_home,parent,false)
        return HomeAdapter.HomeViewHolder(view, viewModel, this)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(newproList[position])
    }
    fun setProduct(setPro: ArrayList<HomeProduct>) {
        productData = setPro;
        newproList.addAll(setPro);
        notifyDataSetChanged()}
    override fun getItemCount(): Int = productData.size
    class HomeViewHolder(
        itemView: View,
        private val viewModel: HomeViewModel,
        private val adapter: HomeAdapter,
    ): RecyclerView.ViewHolder(itemView) {
        private val proName: TextView=itemView.findViewById(R.id.home_pro_name)
        private val proPrice: TextView= itemView.findViewById(R.id.home_pro_price)
        fun bind(homeProduct: HomeProduct) {
//            val saleDescription = purchases.saleDescription;
            proName.text=homeProduct.name;
            proPrice.text="$"+homeProduct.price.toString();
//            purchaseAmount.text="$"+purchases.total.toString();

//            itemView.findViewById<Button>(R.id.btn_history_detail).setOnClickListener {
//                System.out.println(homeProduct.id);
//            }
        }
    }

}