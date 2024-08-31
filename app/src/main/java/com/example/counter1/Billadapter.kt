package com.example.counter1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

interface QuantityUpdateCallback {
    fun updateQuantity(itemName: String, newQuantity: String)
}

class SoldInventory(val name: String, var solditemquantity: Int)


private val addedItems: MutableList<SoldInventory> = mutableListOf()

class Billadapter(private val context: Context,  private val invoiceItems: MutableList<Item>,private val quantityUpdateCallback: QuantityUpdateCallback) : RecyclerView.Adapter<Billadapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = invoiceItems[position]
        holder.itemNameTextView.text = item.name
        holder.quantityTextView.text = "Quantity: ${item.quantity}"
    }

    override fun getItemCount(): Int {
        return invoiceItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.Itemnameinvoice)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityofitemininvoice)
        val addButton: Button = itemView.findViewById(R.id.editfrominvoice)
        init {
            addButton.setOnClickListener {
                // Show a dialog to input quantity
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Enter Quantity")
                val quantityEditText = EditText(context)
                builder.setView(quantityEditText)

                builder.setPositiveButton("Add") { _, _ ->
                    val quantity = quantityEditText.text.toString().toInt()
                    val item = invoiceItems[adapterPosition]
                    if (item.quantity.toInt() >= quantity) {
                        // Add the item to the new list
                        val soldInventory = SoldInventory(item.name, quantity )
                        soldInventory.solditemquantity = quantity
                        addedItems.add(soldInventory)
                        // Subtract the user's added quantity from saved quantity
                        invoiceItems[adapterPosition].quantity = (item.quantity.toInt() - quantity).toString()
                        // Save the item to another file
                        val file = File(context.filesDir, "sold_items.txt")
                        val fileOutputStream = FileOutputStream(file, true)
                        val writer = BufferedWriter(OutputStreamWriter(fileOutputStream))
                        writer.write("${item.name},${quantity}\n")
                        writer.close()
                        // Notify the adapter that the data has changed
                        notifyDataSetChanged()

                        val amount = quantity*(item.sellingprice.toInt())
                        //passing data to billing
                        billedItems(item.name, quantity, item.sellingprice,amount)



                        quantityUpdateCallback.updateQuantity(item.name, item.quantity) // Pass the string values here


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
    }

    fun saveBilledItemToFile(itemName: String, quantity: Int, unitprice: String, amount: Int) {
        val file = File(context?.filesDir, "billed_items.txt")
        val fileOutputStream = FileOutputStream(file, true) // append to the file
        val outputStreamWriter = OutputStreamWriter(fileOutputStream)
        val bufferedWriter = BufferedWriter(outputStreamWriter)

        bufferedWriter.write("$itemName,$quantity,$unitprice,$amount\n")

        bufferedWriter.close()

    }

    fun billedItems(itemName: String, quantity: Int, unitprice: String,amount: Int) {
        saveBilledItemToFile(itemName, quantity, unitprice, amount)
        Log.d("Billed Items", "Item saved to file: $itemName, $quantity, $unitprice, $amount")
    }
}