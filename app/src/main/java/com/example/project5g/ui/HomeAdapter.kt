package com.example.project5g.ui

import android.graphics.Paint
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
                  private val viewModel: HomeViewModel) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var productData: ArrayList<HomeProduct> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_home, parent, false)
        return HomeViewHolder(view, viewModel, this)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(newproList[position])
    }

    fun setProduct(setPro: ArrayList<HomeProduct>) {
        productData = setPro
        newproList.addAll(setPro)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = productData.size

    class HomeViewHolder(
        itemView: View,
        private val viewModel: HomeViewModel,
        private val adapter: HomeAdapter
    ) : RecyclerView.ViewHolder(itemView) {

        private val proName: TextView = itemView.findViewById(R.id.home_pro_name)
        private val proPrice: TextView = itemView.findViewById(R.id.home_pro_price)
        private val discountPriceTextView: TextView = itemView.findViewById(R.id.home_pro_discount_price)
        private val proImg: ImageView = itemView.findViewById(R.id.homeproImg)

        fun bind(homeProduct: HomeProduct?) {
            if (homeProduct != null) {
                val num_format = DecimalFormat("0.00")
                num_format.roundingMode = RoundingMode.CEILING
                proName.text = homeProduct.name

                // Display the original price and apply discount logic
                val discountPercentage = homeProduct.discount / 100.0 // Convert discount percentage to decimal
                val discountPrice = homeProduct.price - (homeProduct.price * discountPercentage)

                if (homeProduct.discount > 0 && discountPrice < homeProduct.price) {
                    // Display the original price with a strikethrough effect
                    proPrice.text = "$${homeProduct.price}"
                    proPrice.setTextColor(itemView.context.getColor(android.R.color.darker_gray)) // Normal price color
                    proPrice.paintFlags = proPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // Add strikethrough effect

                    // Display the discounted price
                    discountPriceTextView.text = String.format("$%.2f", discountPrice) // Format discount price to 2 decimal places
                    discountPriceTextView.visibility = View.VISIBLE
                } else {
                    // No discount, display normal price
                    proPrice.text = "$${homeProduct.price}"
                    proPrice.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark)) // Highlight the price
                    proPrice.paintFlags = proPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() // Remove strikethrough effect
                    discountPriceTextView.visibility = View.GONE
                }

                // Load image using Glide
                val imageUrl = "https://5g.csproject.org/images/${homeProduct.imageURL}"
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
