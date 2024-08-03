package com.example.project5g.ui

import android.app.AlertDialog
import android.graphics.Paint
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project5g.R
import com.example.project5g.data.HomeProduct
import com.example.project5g.viewmodel.HomeViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

class DiscountAdapter(private val discountproList: ArrayList<HomeProduct>,
                      private val viewModel: HomeViewModel): RecyclerView.Adapter<DiscountAdapter.HomeViewHolder>() {

//    private val quantitiesMap: MutableMap<String, Int> = mutableMapOf()
    private  var productData: ArrayList<HomeProduct> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_discount,parent,false)
        return DiscountAdapter.HomeViewHolder(view, viewModel, this)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(discountproList[position])
    }
    fun setProduct(setPro: ArrayList<HomeProduct>) {
        productData = setPro;
        discountproList.addAll(setPro);
        notifyDataSetChanged()}
    override fun getItemCount(): Int = productData.size
    class HomeViewHolder(
        itemView: View,
        private val viewModel: HomeViewModel,
        private val adapter: DiscountAdapter,
    ): RecyclerView.ViewHolder(itemView) {
        private val proName: TextView=itemView.findViewById(R.id.dis_pro_name)
        private val proPrice: TextView= itemView.findViewById(R.id.dis_pro_price)
        private val proImg: ImageView= itemView.findViewById(R.id.disproImg)
        private val proDisPrice: TextView= itemView.findViewById(R.id.item_discount_price)
        private val percent: TextView= itemView.findViewById(R.id.discountPercentage)

        fun bind(homeProduct: HomeProduct?) {
            if (homeProduct != null) {
                println(homeProduct);
                val num_format = DecimalFormat("0.00")
                num_format.roundingMode = RoundingMode.CEILING

                var full_price = homeProduct.price?.toFloat()
                var disprice = homeProduct.discount?.toFloat()
                var calculate_discountPrice = disprice?.let { full_price?.times(it) }?.div(100)?.let { full_price?.minus(it) }
                proName.text = homeProduct.name;
                proPrice.text = "$" + num_format.format(homeProduct.price)
                proPrice.setPaintFlags(proPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                percent.text= "-"+homeProduct.discount.toString()+"%"
                if (full_price != null) {
                    proDisPrice.text ="$"+ num_format.format(calculate_discountPrice)
                }
                val imageUrl = "https://5g.csproject.org/images/${homeProduct.imageURL}"
                // Load image using Glide or another image loading library
                Glide.with(itemView)
                    .load(imageUrl)
                    .into(proImg)
            itemView.findViewById<Button>(R.id.btn_close).setOnClickListener {
                homeProduct.id?.let { productId ->
                    viewModel.addToCart(productId)
                    showSuccessDialog()
                }
            }
            }
        }
        private fun showSuccessDialog() {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle("Success")
                .setMessage("Item added to cart successfully!")
            val dialog = builder.create()
            dialog.show()
            // Delay the dismissal of the dialog
            val handler = Handler()
            handler.postDelayed({
                dialog.dismiss()
            }, 1000) // 1000 milliseconds = 1 second
        }
    }

}