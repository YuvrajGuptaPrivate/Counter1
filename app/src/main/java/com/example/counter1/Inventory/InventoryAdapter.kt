// InventoryAdapter.kt
package com.example.counter1.Inventory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.counter1.Db.InventoryDataClass
import com.example.counter1.R

class InventoryAdapter(private val inventoryFragment: Inventory) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>() {

    private var inventoryList: ArrayList<InventoryDataClass> = ArrayList()



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.Itemnameoutput)
        val editButton: ImageView = itemView.findViewById(R.id.edit)
        val removeButton: ImageView = itemView.findViewById(R.id.Remove)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityoutput)
        val editQuantityEditText: EditText = itemView.findViewById(R.id.edit_quantity)
        val saveButton: Button = itemView.findViewById(R.id.save)
        val priceTextView: TextView = itemView.findViewById(R.id.priceoutput)

        fun bind(inventoryData: InventoryDataClass) {
            textView.text = inventoryData.itemName
            quantityTextView.text = "Quantity: ${inventoryData.itemstocks}"
            priceTextView.text = "Price: ${inventoryData.itemprice}"
        }


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
                        val itemName = inventoryList[position].itemName
                        inventoryFragment.updateItemQuantity(itemName!!, newQuantity.toInt())

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
                    val itemName = inventoryList[position].itemName
                    inventoryFragment.removeItem(itemName!!)
                    notifyItemRemoved(position)
                    notifyDataSetChanged()
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(inventoryList[position])
    }

    override fun getItemCount(): Int {
        return inventoryList.size
    }

    fun updateInventoryList(inventoryData: List<InventoryDataClass>) {
        inventoryList.clear()
        inventoryList.addAll(inventoryData)
        notifyDataSetChanged()
    }
}