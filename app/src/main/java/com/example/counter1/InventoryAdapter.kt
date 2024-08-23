// InventoryAdapter.kt
package com.example.counter1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InventoryAdapter(private val inventoryData: ArrayList<Item>) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.Items)
        val editButton: Button = itemView.findViewById(R.id.edit)
        val removeButton: Button = itemView.findViewById(R.id.Remove)

        init {
            editButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val itemToEdit = inventoryData[position]
                    val newItem = Item("Edited ${itemToEdit.name}", itemToEdit.quantity, itemToEdit.price, itemToEdit.description)
                    inventoryData[position] = newItem
                    notifyItemChanged(position)
                }
            }

            removeButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    inventoryData.removeAt(position)
                    notifyItemRemoved(position)
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
    }

    override fun getItemCount(): Int {
        return inventoryData.size
    }
}