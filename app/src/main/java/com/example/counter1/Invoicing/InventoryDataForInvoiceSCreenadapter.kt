package com.example.counter1.Invoicing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.counter1.Db.InventoryDataClass
import com.example.counter1.Db.Sales
import com.example.counter1.R


class InventoryDataForInvoiceSCreenadapter(private val activity: InvoiceScreen,private val context: Context) : RecyclerView.Adapter<InventoryDataForInvoiceSCreenadapter.ViewHolder>() {


    private var inventoryList: ArrayList<InventoryDataClass> = ArrayList()

    // MutableLiveData to hold the ordered items
    private val _orderedItemsList = MutableLiveData<List<OrderedItem>>(emptyList())

    // Expose as LiveData<List<OrderedItem>>
    val orderedItemsList: LiveData<List<OrderedItem>> = _orderedItemsList


    private var _totalCartValue = MutableLiveData<Long>(0)
    val totalCartValue: LiveData<Long> get() = _totalCartValue



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = inventoryList[position]
        holder.itemNameTextView.text = item.itemName
        holder.quantityTextView.text = "Quantity: ${item.itemstocks}"
    }

    override fun getItemCount(): Int {
        return inventoryList.size
    }

    fun updateInventoryList(inventoryData: List<InventoryDataClass>) {
        inventoryList.clear()
        inventoryList.addAll(inventoryData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.Itemnameinvoice)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityofitemininvoice)
        val addButton: ImageButton = itemView.findViewById(R.id.editfrominvoice)

        init {
            addButton.setOnClickListener {
                // Show a dialog to input quantity
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Enter Quantity")
                val quantityEditText = EditText(context)
                builder.setView(quantityEditText)

                builder.setPositiveButton("Add") { _, _ ->
                    val quantity = quantityEditText.text.toString().toInt()
                    val item = inventoryList[adapterPosition]
                    if (item.itemstocks >= quantity) {
                        val price = (item.itemprice)
                        // Subtract the user's added quantity from saved quantity
                        val newquantiy = (item.itemstocks - quantity)
                        activity.updateQuantity(item.itemName,newquantiy)
                        val sales = Sales(item.itemId,item.itemprice,quantity)
                        activity.insertSalesData(sales)
                        addOrderedItem(item.itemId,item.itemName, quantity,item.itemprice.toInt())
                        updateTotalCartValue(price, quantity)



                        notifyDataSetChanged()

                    } else {
                        // Show an error message if the selected quantity is more than the actual quantity
                        Toast.makeText(context, "Not enough quantity in stock", Toast.LENGTH_SHORT).show()
                    }
                }


                builder.setNegativeButton("Cancel") { _, _ ->
                    // Cancel button clicked
                }

                builder.show()
            }
        }
        // Function to add one ordered item at a time
        fun addOrderedItem(itemId: Int, itemName: String, quantity: Int,unitprice : Int) {
            val newItem = OrderedItem(itemId = itemId, itemName = itemName, quantity = quantity,unitprice)
            val currentList = _orderedItemsList.value?.toMutableList() ?: mutableListOf() // Get the current list or create a new one
            currentList.add(newItem) // Add the new item
            _orderedItemsList.value = currentList // Update LiveData with the new list
        }


        fun updateTotalCartValue(price: Long, quantity: Int) {
            _totalCartValue.value = (_totalCartValue.value ?: 0) + (price * quantity)
        }

    }


}