package com.example.counter1.Invoicing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.counter1.R

class BillItemAdapter(private val context: Context, private var billItems: List<OrderedItem>) :
    RecyclerView.Adapter<BillItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bill_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = billItems[position]
        holder.nameTextView.text = item.itemName
        holder.quantityTextView.text = item.quantity.toString()
        holder.unitPriceTextView.text = "Unit Price: ${item.unitprice}"
        holder.priceTextView.text = "Total Price: ${item.unitprice * item.quantity}"
    }



    override fun getItemCount(): Int {
        return billItems.size
    }
    fun updateItems(newItems: List<OrderedItem>) {
        billItems = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameofbilleditem)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityofbilleditem)
        val unitPriceTextView: TextView = itemView.findViewById(R.id.UnitPrice)
        val priceTextView: TextView = itemView.findViewById(R.id.priceofbilleditem)
    }
}