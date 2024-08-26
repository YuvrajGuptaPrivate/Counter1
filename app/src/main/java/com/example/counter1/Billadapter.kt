package com.example.counter1

import android.content.Context
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

class InvoiceItem(val name: String, var solditemquantity: Int)
private val addedItems: MutableList<InvoiceItem> = mutableListOf()

class Billadapter(private val context: Context, private val invoiceItems: MutableList<Item>) : RecyclerView.Adapter<Billadapter.ViewHolder>() {

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
                        val invoiceItem = InvoiceItem(item.name, 0)
                        invoiceItem.solditemquantity = quantity
                        addedItems.add(invoiceItem)
                        // Subtract the user's added quantity from saved quantity
                        invoiceItems[adapterPosition].quantity = (item.quantity.toInt() - quantity).toString()
                        // Save the item to another file
                        val file = File(context.filesDir, "added_items.txt")
                        val fileOutputStream = FileOutputStream(file, true)
                        val writer = BufferedWriter(OutputStreamWriter(fileOutputStream))
                        writer.write("${item.name},${quantity}\n")
                        writer.close()
                        // Notify the adapter that the data has changed
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
    }
}