package com.example.counter1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.*
data class Item(val name: String, var quantity: String, var sellingprice: String, val description: String)

class Inventory : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var inventoryData: ArrayList<Item>
    private lateinit var recyclerViewAdapter: InventoryAdapter
    private lateinit var inputFields: LinearLayout
    private lateinit var saveItemButton: Button
    private lateinit var itemNameEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var sellingpriceEditText: EditText
    private lateinit var descriptionEditText: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)

        recyclerView = view.findViewById(R.id.recycelerviewforinventorymanagemnt)
        inputFields = view.findViewById(R.id.input_fields)
        saveItemButton = view.findViewById(R.id.save_item)
        itemNameEditText = view.findViewById(R.id.item_name)
        quantityEditText = view.findViewById(R.id.quantity)
        sellingpriceEditText = view.findViewById(R.id.Sellingprice)
        descriptionEditText = view.findViewById(R.id.description)
        // Load data from internal storage
        inventoryData = loadInventoryData()

        recyclerViewAdapter = InventoryAdapter(this,inventoryData)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Add button click listeners
        val addButton = view.findViewById<Button>(R.id.additem)
        addButton.setOnClickListener {
            inputFields.visibility = View.VISIBLE
        }
        saveItemButton.setOnClickListener {
            val itemName = itemNameEditText.text.toString()
            val quantity = quantityEditText.text.toString()
            val sellingprice = sellingpriceEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val item = Item(itemName, quantity, sellingprice, description)
            inventoryData.add(item)

            // Update the RecyclerView
            updateInventoryData(inventoryData)
            recyclerViewAdapter.notifyDataSetChanged() // Add this line

            // Reset the input fields
            itemNameEditText.setText(resources.getString(R.string.empty_string))
            quantityEditText.setText(resources.getString(R.string.empty_string))
            sellingpriceEditText.setText(resources.getString(R.string.empty_string))
            descriptionEditText.setText(resources.getString(R.string.empty_string))

        }

        return view

    }

    override fun onPause() {
        super.onPause()
        saveInventoryData(inventoryData)
    }

    override fun onResume() {
        super.onResume()
        inventoryData = loadInventoryData()
        recyclerViewAdapter.notifyDataSetChanged()
    }

    fun loadInventoryData(): ArrayList<Item> {
        val file = File(requireContext().filesDir, "inventory_data.txt")
        val data = ArrayList<Item>()

        if (file.exists()) {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                val parts = line!!.split(",")
                data.add(Item(parts[0], parts[1], parts[2], parts[3]))
            }

            bufferedReader.close()
        }

        return data
    }

    private fun saveInventoryData(data: ArrayList<Item>) {
        val file = File(requireContext().filesDir, "inventory_data.txt")
        val fileOutputStream = FileOutputStream(file)
        val outputStreamWriter = OutputStreamWriter(fileOutputStream)
        val bufferedWriter = BufferedWriter(outputStreamWriter)

        for (item in data) {
            bufferedWriter.write("${item.name},${item.quantity},${item.sellingprice},${item.description}\n")
        }
        bufferedWriter.close()
    }

    private fun updateInventoryData(newData: ArrayList<Item>) {
        saveInventoryData(newData)
        inventoryData = newData
        recyclerViewAdapter.notifyItemInserted(newData.size - 1)
    }

    private fun RefreshInventoryData(newData: ArrayList<Item>) {
        saveInventoryData(newData)
        recyclerViewAdapter.inventoryData = newData
        recyclerViewAdapter.notifyDataSetChanged()
    }

     fun updateItemQuantity(itemName: String, newQuantity: String) {
        // Find the item in the inventory data
        val itemToUpdate = inventoryData.find { it.name == itemName }
        if (itemToUpdate != null) {
            // Update the quantity of the item
            itemToUpdate.quantity = newQuantity
            // Save the updated inventory data
            RefreshInventoryData(inventoryData)
        }
    }
    fun removeItem(itemName: String) {
        inventoryData.removeAll { it.name == itemName }
        saveInventoryData(inventoryData)
        recyclerViewAdapter.notifyDataSetChanged()
    }





}