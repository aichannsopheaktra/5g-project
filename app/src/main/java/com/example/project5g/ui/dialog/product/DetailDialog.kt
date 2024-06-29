package com.example.project5g.ui.dialog.product

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
        val exitButton: Button = dialogView.findViewById(R.id.exit_button)
        val addToCartButton: Button = dialogView.findViewById(R.id.add_to_cart_button)
//        val detailDescription: TextView = dialogView.findViewById(R.id.detail_description)

        // Load the image using Glide
        Glide.with(context)
            .load("https://5g.csproject.org/images/${item.imageURL}")
            .into(detailImage)

        // Set the product details
        detailName.text = item.name
        detailPrice.text = "$" + item.price.toString()
//        detailDescription.text = item.description

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
            item.id?.let { productId ->
                viewModel.addToCart(productId)
                showSuccessDialog()
            }
        }

        dialog.show()

        // Make the dialog fullscreen
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun showSuccessDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Success")
            .setMessage("Item added to cart successfully!")
        val dialog = builder.create()
        dialog.show()
        val handler = Handler()
        handler.postDelayed({
            dialog.dismiss()
        }, 1000)
    }
}
