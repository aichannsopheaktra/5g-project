package com.example.project5g.ui.dialog.product

import android.app.AlertDialog
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.project5g.R
import com.example.project5g.data.Product
import com.example.project5g.viewmodel.HomeViewModel

class DetailDialog(private val context: Context, private val item: Product, private val viewModel: HomeViewModel) {

    fun show() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_product_detail, null)
        val detailImage: ImageView = dialogView.findViewById(R.id.detail_image)
        val detailName: TextView = dialogView.findViewById(R.id.detail_name)
        val detailPrice: TextView = dialogView.findViewById(R.id.detail_price)
        val detailDiscountPrice: TextView = dialogView.findViewById(R.id.detail_discount_price)
        val detailCategory: TextView = dialogView.findViewById(R.id.detail_category)
        val exitButton: Button = dialogView.findViewById(R.id.exit_button)
        val addToCartButton: Button = dialogView.findViewById(R.id.add_Button)

        // Load the image using Glide
        Glide.with(context)
            .load("https://5g.csproject.org/images/${item.imageURL}")
            .into(detailImage)

        // Set the product details
        detailName.text = item.name
        detailCategory.text = item.category.name

        val discountPercentage = item.discount / 100.0 // Convert discount percentage to decimal
        val discountPrice = item.price - (item.price * discountPercentage)

        if (item.discount > 0 && discountPrice < item.price) {
            detailPrice.text = "$"+item.price.toString()
            detailPrice.setTextColor(context.getColor(android.R.color.darker_gray)) // Normal price color
            detailPrice.paintFlags = detailPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG // Add strikethrough effect
            detailDiscountPrice.text = String.format("$%.2f", discountPrice) // Format discount price to 2 decimal places
            detailDiscountPrice.visibility = View.VISIBLE
        } else {
            detailPrice.text = "$"+item.price.toString()
            detailPrice.setTextColor(context.getColor(android.R.color.holo_red_dark))
            detailPrice.paintFlags = detailPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() // Remove strikethrough effect
            detailDiscountPrice.visibility = View.GONE
        }
        // Create and show the dialog
        val builder = AlertDialog.Builder(context, R.style.FullscreenDialogTheme)
        builder.setView(dialogView)
        val dialog = builder.create()

        // Set the exit button action
        exitButton.setOnClickListener {
            dialog.dismiss()
        }

        // Set the add to cart button action
        addToCartButton.setOnClickListener {
            item.id.let { productId ->
                viewModel.addToCart(productId)
                Toast.makeText(context, "Item added to cart successfully!", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()

        // Make the dialog fullscreen
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
