package com.example.project5g.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project5g.R
import com.example.project5g.data.HomeProduct
import com.example.project5g.viewmodel.HomeViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

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
        private val proImg: ImageView= itemView.findViewById(R.id.homeproImg)

//        private val cardView2: View = itemView.findViewById(R.id.cardView2)
        fun bind(homeProduct: HomeProduct?) {
            if (homeProduct != null) {
//            val saleDescription = purchases.saleDescription;
                val num_format = DecimalFormat("0.00")
                num_format.roundingMode = RoundingMode.CEILING
                proName.text = homeProduct.name;
                proPrice.text = "$" +num_format.format(homeProduct.price)

                val imageUrl = "https://5g.csproject.org/images/${homeProduct.imageURL}"
                // Load image using Glide or another image loading library
                Glide.with(itemView)
                    .load(imageUrl)
                    .into(proImg)
            itemView.findViewById<Button>(R.id.btn_close).setOnClickListener {
                homeProduct.id?.let { productId ->
                    viewModel.addToCart(productId)
                    Toast.makeText(itemView.context, "Item added to cart successfully!", Toast.LENGTH_SHORT).show()
                }
            }
            }
        }
    }
}