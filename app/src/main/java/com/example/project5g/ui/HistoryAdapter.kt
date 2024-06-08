package com.example.project5g.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project5g.R
import com.example.project5g.data.CartItem
import com.example.project5g.data.Product
import com.example.project5g.data.Purchases
import com.example.project5g.data.SaleDescription
import com.example.project5g.viewmodel.HomeViewModel

class HistoryAdapter(
    private val purChaseList: ArrayList<Purchases>,
    private val viewModel: HomeViewModel) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>()

{
    private val quantitiesMap: MutableMap<String, Int> = mutableMapOf()
    private  var purchasesData: ArrayList<Purchases> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history,parent,false)
        return HistoryViewHolder(view,viewModel,this)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(purChaseList[position]);
    }

    fun setPurchase(setPurchase: ArrayList<Purchases>) {
//        purchasesData = setPurchase;
        purChaseList.addAll(setPurchase);
        notifyDataSetChanged()
//
//        cartItems.clear()
//        cartItems.addAll(setCartItems)
//        cartItems.forEach { cartItem ->
//            cartItem.product?.id?.let { productId ->
//                quantitiesMap[productId] = cartItem.qty
//            }
//        }
//        notifyDataSetChanged()
//        updateTotal()

    }
    override fun getItemCount(): Int = purChaseList.size
    class HistoryViewHolder(
        itemView:View,
        private val viewModel: HomeViewModel,
        private val adapter: HistoryAdapter,
        ): RecyclerView.ViewHolder(itemView) {
        private val purchasesDate: TextView = itemView.findViewById(R.id.txt_his_date);
        private val purchaseAmount: TextView = itemView.findViewById(R.id.txt_his_amount);

        fun bind(purchases: Purchases) {
//            val saleDescription = purchases.saleDescription;
            purchasesDate.text=purchases.date.toString();
            purchaseAmount.text="$"+purchases.total.toString();
        }
    }

}
