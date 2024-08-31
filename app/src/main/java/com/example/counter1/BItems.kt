package com.example.counter1
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView


class BillItemsAdapter(private val context: Context, private val billItems: List<BilledItem>) : RecyclerView.Adapter<BillItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bill_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val billItem = billItems[position]
        holder.nameTextView.text = billItem.itemName
        holder.unitpriceTextView.text = billItem.unitprice.toString()
        holder.quanityTextView.text = billItem.quantity.toString()
        holder.amountTextView.text = billItem.amount.toString()
    }

    override fun getItemCount(): Int {
        return billItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameofbilleditem)
        val unitpriceTextView: TextView = itemView.findViewById(R.id.UnitPrice)
        val quanityTextView: TextView = itemView.findViewById(R.id.quantityofbilleditem)
        val amountTextView : TextView = itemView.findViewById(R.id.priceofbilleditem)

    }
}