package com.example.project5g.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project5g.R
import com.example.project5g.data.Purchases
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryAdapter(
    val context: Context,
    private val purChaseList: ArrayList<Purchases>,
    val fragment: HistoryFragment
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(purChaseList[position], fragment)
    }

    fun setPurchase(newPurchases: ArrayList<Purchases>) {
        val startPosition = purChaseList.size
        purChaseList.addAll(newPurchases)
        notifyItemRangeInserted(startPosition, newPurchases.size)
    }

    // Add this method to clear purchases
    fun clearPurchases() {
        purChaseList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = purChaseList.size
    var test = HistoryFragment()

    class HistoryViewHolder(
        itemView:View,
        ): RecyclerView.ViewHolder(itemView) {
        private val purchasesDate: TextView = itemView.findViewById(R.id.his_datetime);
        private val purchaseAmount: TextView = itemView.findViewById(R.id.txt_his_amount);


        fun bind(purchases: Purchases, fragment: HistoryFragment) {

            val parsedDate = LocalDateTime.parse(purchases.date.toString(), DateTimeFormatter.ISO_DATE_TIME)
            val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("MM.dd.yyyy  HH:mm"))
            val num_format = DecimalFormat("0.00")
            num_format.roundingMode = RoundingMode.CEILING

            purchasesDate.text= formattedDate
            purchaseAmount.text="$"+num_format.format(purchases.total);
            var receiptList = purchases.saleDescription;
            itemView.findViewById<Button>(R.id.btn_history_detail).setOnClickListener {
//            fragment.showDialog();
                if (receiptList != null) {
//                disPlayreceipt(receiptList)
                fragment.showDialog(purchases)

            }

        }}
//        val dialog =  Dialog(this);
//        fun disPlayreceipt(receipt: List<SaleDescription>){
//
//            println("btn is worked")
//            for (i in receipt) {
//                println(i);
//            }
//
//    }

}
//     fun abc() {
//         var menu: PopupMenu?
//         menu = PopupMenu(this.context, it)
//         menu.inflate(R.menu.card_menu)
//
//         menu.setOnMenuItemClickListener {
//             when(it.itemId) {
//                 R.id.menu_modify -> { Toast.makeText(context, "Modify", Toast.LENGTH_LONG).show()
//                     true }
//
//                 R.id.menu_delete -> { Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show()
//                     true }
//
//                 else -> false
//             }
//         }
//
//         menu.show()
//    }



    }

