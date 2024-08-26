// InventoryAdapter.kt
package com.example.counter1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InventoryAdapter(private val inventoryFragment: Inventory, var inventoryData: ArrayList<Item> ) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.Itemnameoutput)
        val editButton: ImageView = itemView.findViewById(R.id.edit)
        val removeButton: ImageView = itemView.findViewById(R.id.Remove)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityoutput)
        val editQuantityEditText: EditText = itemView.findViewById(R.id.edit_quantity)
        val saveButton: Button = itemView.findViewById(R.id.save)
        val priceTextView: TextView = itemView.findViewById(R.id.priceoutput)


        init {
            editButton.setOnClickListener {
                quantityTextView.visibility = View.GONE
                editQuantityEditText.visibility = View.VISIBLE
                saveButton.visibility = View.VISIBLE
                editButton.visibility = View.GONE
            }

            saveButton.setOnClickListener {
                val newQuantity = editQuantityEditText.text.toString()
                if (newQuantity.isNotEmpty()) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val itemName = inventoryData[position].name
                        inventoryFragment.updateItemQuantity(itemName, newQuantity)

                        quantityTextView.text = "Quantity: $newQuantity" // Update the quantityTextView
                        quantityTextView.visibility = View.VISIBLE
                        editQuantityEditText.visibility = View.GONE
                        saveButton.visibility = View.GONE
                        editButton.visibility = View.VISIBLE

                        notifyItemChanged(position)
                    }
                }
            }
            removeButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    inventoryData.removeAt(position)
                    notifyItemRemoved(position)
                    notifyDataSetChanged() // Update the RecyclerView

                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = inventoryData[position].name
        holder.quantityTextView.text = "Quantity: ${inventoryData[position].quantity}"
        holder.priceTextView.text = "Price: ${inventoryData[position].price}"




    }

    override fun getItemCount(): Int {
        return inventoryData.size
    }
}