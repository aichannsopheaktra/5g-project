package com.example.project5g.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project5g.R
import com.example.project5g.data.Product
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.ui.dialog.product.DetailDialog

class ProductAdapter(private val viewModel: HomeViewModel) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private var prodata: ArrayList<Product>? = null
    private var originalProData: ArrayList<Product>? = null
    private var filteredProData: ArrayList<Product>? = null
    private var currentQuery: String = ""
    private var currentCategoryId: String? = null

    fun filterByCategory(categoryId: String) {
        currentCategoryId = categoryId
        applyFilters()
    }

    fun filter(query: String) {
        currentQuery = query
        applyFilters()
    }

    fun setProData(list: ArrayList<Product>?) {
        prodata = list
        originalProData = ArrayList(list ?: emptyList())
        applyFilters()
    }

    fun resetCategoryFilter() {
        currentCategoryId = null
        applyFilters()
    }

    private fun applyFilters() {
        filteredProData = originalProData?.let { products ->
            var filteredList = products.toMutableList()

            currentCategoryId?.let { categoryId ->
                filteredList = filteredList.filter { product ->
                    product.categoryId?.contains(categoryId, ignoreCase = true) ?: false
                }.toMutableList()
            }

            if (currentQuery.isNotBlank()) {
                filteredList = filteredList.filter { product ->
                    product.name?.contains(currentQuery, ignoreCase = true) ?: false
                }.toMutableList()
            }

            ArrayList<Product>(filteredList) // Explicit conversion to Kotlin ArrayList
        } ?: ArrayList()

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_card, parent, false)
        return ProductViewHolder(view, viewModel)
    }

    override fun getItemCount(): Int {
        return if (filteredProData == null) 0 else Math.ceil((filteredProData!!.size / 2.0)).toInt()
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val index1 = position * 2
        val index2 = index1 + 1

        holder.bindView1(filteredProData?.getOrNull(index1))
        holder.bindView2(filteredProData?.getOrNull(index2))
    }

    class ProductViewHolder(
        itemView: View,
        private val viewModel: HomeViewModel
    ) : RecyclerView.ViewHolder(itemView) {
        private val pName1: TextView = itemView.findViewById(R.id.item_name1)
        private val pPrice1: TextView = itemView.findViewById(R.id.item_price1)
        private val pDiscountPrice1: TextView = itemView.findViewById(R.id.item_discount_price1)
        private val imageView1: ImageView = itemView.findViewById(R.id.imageView1)
        private val cardView1: View = itemView.findViewById(R.id.cardView1)



        private val pName2: TextView = itemView.findViewById(R.id.item_name2)
        private val pPrice2: TextView = itemView.findViewById(R.id.item_price2)
        private val pDiscountPrice2: TextView = itemView.findViewById(R.id.item_discount_price2)
        private val imageView2: ImageView = itemView.findViewById(R.id.imageView2)
        private val cardView2: View = itemView.findViewById(R.id.cardView2)

        fun bindView1(item: Product?) {
            if (item != null) {
                pName1.text = item.name
                pPrice1.text = "$" + item.price.toString()
                val discountPercentage = item.discount / 100.0
                val discountPrice = item.price - (item.price * discountPercentage)
//                println(item.price.toString() + "full")
//                println(item.discount.toString() + "%")
//                println(discountPercentage.toString() + "%.o")
//                println(discountPrice.toString() + "dis")

                if (item.discount > 0 && discountPrice < item.price) {
                    pDiscountPrice1.text = "$" + String.format("%.2f", discountPrice)
                    pDiscountPrice1.visibility = View.VISIBLE
                    pPrice1.setTextColor(itemView.context.getColor(android.R.color.darker_gray)) // Normal price color
                    pPrice1.paintFlags = pPrice1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    pDiscountPrice1.visibility = View.GONE
                    pPrice1.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark)) // Red color if no discount
                    pPrice1.paintFlags = pPrice1.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() // Remove strikethrough effect
                }

                val imageUrl = "https://5g.csproject.org/images/${item.imageURL}"
                Glide.with(itemView)
                    .load(imageUrl)
                    .into(imageView1)
                cardView1.setOnClickListener {
                    DetailDialog(itemView.context, item, viewModel).show()
                }
                itemView.visibility = View.VISIBLE
            } else {
                itemView.visibility = View.GONE
            }
        }

        fun bindView2(item: Product?) {
            if (item != null) {
                pName2.text = item.name
                pPrice2.text = "$" + item.price.toString()
                val discountPercentage = item.discount / 100.0
                val discountPrice = item.price - (item.price * discountPercentage)
                if (item.discount > 0 && discountPrice < item.price){
                    pDiscountPrice2.text = "$" + String.format("%.2f", discountPrice)
                    pDiscountPrice2.visibility = View.VISIBLE
                    pPrice2.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
                    pPrice2.paintFlags = pPrice2.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    pDiscountPrice2.visibility = View.GONE
                    pPrice2.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
                    pPrice2.paintFlags = pPrice2.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() // Remove strikethrough effect
                }
                val imageUrl = "https://5g.csproject.org/images/${item.imageURL}"
                Glide.with(itemView)
                    .load(imageUrl)
                    .into(imageView2)
                cardView2.setOnClickListener {
                    DetailDialog(itemView.context, item, viewModel).show()
                }
                cardView2.visibility = View.VISIBLE
            } else {
                cardView2.visibility = View.INVISIBLE
            }
        }
    }
}
