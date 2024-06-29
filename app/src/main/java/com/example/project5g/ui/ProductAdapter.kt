package com.example.project5g.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project5g.R
import com.example.project5g.data.Product
import com.example.project5g.viewmodel.HomeViewModel
import com.example.project5g.ui.dialog.product.DetailDialog


class ProductAdapter(private val viewModel: HomeViewModel) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private var prodata: ArrayList<Product>? = null
    private var filteredProData: ArrayList<Product>? = null

    fun setProData(list: ArrayList<Product>?) {
        prodata = list
        filteredProData = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_card, parent, false)
        return ProductViewHolder(view, viewModel)
    }

    override fun getItemCount(): Int {
        return if (filteredProData == null) 0 else Math.ceil((filteredProData!!.size / 2.0)).toInt()
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val index1 = position * 2
        val index2 = index1 + 1

        holder.bindView1(filteredProData?.getOrNull(index1))
        if (index2 < filteredProData!!.size) {
            holder.bindView2(filteredProData?.getOrNull(index2))
        } else {
            holder.hideView2()
        }
    }

    fun filter(query: String) {
        filteredProData = prodata?.filter { product ->
            product.name?.contains(query, ignoreCase = true) ?: false
        } as ArrayList<Product>?
        notifyDataSetChanged()
    }

    class ProductViewHolder(itemView: View, private val viewModel: HomeViewModel) : RecyclerView.ViewHolder(itemView) {
        private val pName1: TextView = itemView.findViewById(R.id.item_name1)
        private val pPrice1: TextView = itemView.findViewById(R.id.item_price1)
        private val imageView1: ImageView = itemView.findViewById(R.id.imageView1)

        private val pName2: TextView = itemView.findViewById(R.id.item_name2)
        private val pPrice2: TextView = itemView.findViewById(R.id.item_price2)
        private val imageView2: ImageView = itemView.findViewById(R.id.imageView2)
        private val cardView2: View = itemView.findViewById(R.id.cardView2)

        fun bindView1(item: Product?) {
            if (item != null) {
                pName1.text = item.name
                pPrice1.text = "$" + item.price.toString()
                val imageUrl = "https://5g.csproject.org/images/${item.imageURL}"
                Glide.with(itemView)
                    .load(imageUrl)
                    .into(imageView1)
                itemView.findViewById<Button>(R.id.addButton1).setOnClickListener {
                    item.id?.let { productId ->
                        viewModel.addToCart(productId)
                        DetailDialog(itemView.context, item, viewModel).showSuccessDialog()
                    }
                }
                imageView1.setOnClickListener {
                    DetailDialog(itemView.context, item, viewModel).show()
                }
            }
        }

        fun bindView2(item: Product?) {
            if (item != null) {
                pName2.text = item.name
                pPrice2.text = "$" + item.price.toString()
                val imageUrl = "https://5g.csproject.org/images/${item.imageURL}"
                Glide.with(itemView)
                    .load(imageUrl)
                    .into(imageView2)
                itemView.findViewById<Button>(R.id.addButton2).setOnClickListener {
                    item.id?.let { productId ->
                        viewModel.addToCart(productId)
                        DetailDialog(itemView.context, item, viewModel).showSuccessDialog()
                    }
                }
                imageView2.setOnClickListener {
                    DetailDialog(itemView.context, item, viewModel).show()
                }
                cardView2.visibility = View.VISIBLE
            }
        }
        fun hideView2() {
            cardView2.visibility = View.INVISIBLE
        }
    }
}
