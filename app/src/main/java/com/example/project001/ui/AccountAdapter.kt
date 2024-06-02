package com.example.project001.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project001.R
import com.example.project001.data.Customer

class AccountAdapter(private var customers: ArrayList<Customer>) : RecyclerView.Adapter<AccountAdapter.CustomerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(customers[position])
    }

    override fun getItemCount(): Int = customers.size

    fun setCustomerData(newCustomers: ArrayList<Customer>) {
        customers.clear()
        customers.addAll(newCustomers)
        notifyDataSetChanged()
    }

    class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val customerName: TextView = itemView.findViewById(R.id.customerName)
        private val customerEmail: TextView = itemView.findViewById(R.id.customerEmail)

        fun bind(customer: Customer) {
            customerName.text = customer.name
            customerEmail.text = customer.email
            // Bind other fields as needed
        }
    }
}
