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
import com.example.project5g.viewmodel.HomeViewModel

class CartAdapter(
    private val cartItems: ArrayList<CartItem>,
    private val totalTextView: TextView,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val quantitiesMap: MutableMap<String, Int> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view, viewModel, this)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    fun setCartItems(setCartItems: ArrayList<CartItem>) {
        cartItems.clear()
        cartItems.addAll(setCartItems)
        cartItems.forEach { cartItem ->
            cartItem.product?.id?.let { productId ->
                quantitiesMap[productId] = cartItem.qty
            }
        }
        notifyDataSetChanged()
        updateTotal()
    }

    fun updateTotal() {
        var totalPrice = 0.0
        for (cartItem in cartItems) {
            val product = cartItem.product
            val price = product?.price
            val qty = quantitiesMap[product?.id] ?: cartItem.qty
            if (product != null && price != null) {
                totalPrice += price * qty
            }
        }
        totalTextView.text = "$$totalPrice"
    }

    fun removeItem(position: Int) {
        val cartItem = cartItems[position]
        cartItem.product?.id?.let {
            quantitiesMap.remove(it)
        }
        cartItems.removeAt(position)
        notifyItemRemoved(position)
        updateTotal()
    }

    class CartViewHolder(
        itemView: View,
        private val viewModel: HomeViewModel,
        private val adapter: CartAdapter
    ) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        private val qtyTextView: TextView = itemView.findViewById(R.id.qtyTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.cartImageView)

        fun bind(cartItem: CartItem) {
            val product = cartItem.product
            productNameTextView.text = product?.name
            qtyTextView.text = cartItem.qty.toString()
            priceTextView.text = "$${product?.price}"
            val imageUrl = "https://5g.csproject.org/images/${product?.imageURL}"
            Glide.with(itemView)
                .load(imageUrl)
                .into(imageView)

            itemView.findViewById<ImageButton>(R.id.increaseButton).setOnClickListener {
                product?.id?.let { productId ->
                    val currentQty = qtyTextView.text.toString().toInt()
                    viewModel.addToCart(productId)
                    val newQty = currentQty + 1
                    qtyTextView.text = newQty.toString()
                    adapter.quantitiesMap[productId] = newQty
                    adapter.updateTotal()
                }
            }

            itemView.findViewById<ImageButton>(R.id.decreaseButton).setOnClickListener {
                product?.id?.let { productId ->
                    val currentQty = qtyTextView.text.toString().toInt()
                    viewModel.reduceCart(productId)
                    val newQty = currentQty - 1
                    if (newQty >= 1) {
                        qtyTextView.text = newQty.toString()
                        adapter.quantitiesMap[productId] = newQty
                        adapter.updateTotal()
                    } else {
                        adapter.removeItem(adapterPosition)
                    }
                }
            }
        }
    }
}
