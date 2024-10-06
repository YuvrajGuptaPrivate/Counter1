package com.example.counter1.Inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.counter1.Db.AppDATABASE
import com.example.counter1.Db.InventoryDao

import com.example.counter1.Db.InventoryDataClass
import com.example.counter1.R
import java.text.SimpleDateFormat

import java.util.Calendar
import java.util.Locale


class Inventory : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: InventoryAdapter
    private lateinit var inputFields: LinearLayout
    private lateinit var saveItemButton: Button
    private lateinit var itemNameEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var sellingpriceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var backbutton : Button

    private lateinit var viewModel: InventoryViewModel
    private lateinit var inventoryDao: InventoryDao


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)

        recyclerView = view.findViewById(R.id.recycelerviewforinventorymanagemnt)
        inputFields = view.findViewById(R.id.input_fields)
        saveItemButton = view.findViewById(R.id.save_item)
        itemNameEditText = view.findViewById(R.id.item_name)
        quantityEditText = view.findViewById(R.id.quantity)
        sellingpriceEditText = view.findViewById(R.id.Sellingprice)
        descriptionEditText = view.findViewById(R.id.description)
        backbutton = view.findViewById(R.id.back)

        inventoryDao = AppDATABASE.getinstance(requireContext()).inventoryDao()
        val factory = InventoryViewModelFactory(inventoryDao)
        viewModel = ViewModelProvider(this,factory).get(InventoryViewModel::class.java)


        recyclerViewAdapter = InventoryAdapter(this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.AllInventory.observe(viewLifecycleOwner, Observer { inventory ->
            recyclerViewAdapter.updateInventoryList(inventory)
            recyclerViewAdapter.notifyDataSetChanged()
        })

        // Add button click listeners
        val addButton = view.findViewById<Button>(R.id.additem)
        addButton.setOnClickListener {
            inputFields.visibility = View.VISIBLE
        }
        saveItemButton.setOnClickListener {
            val itemName = itemNameEditText.text.toString()
            val quantity = quantityEditText.text.toString().toInt()
            val sellingprice = sellingpriceEditText.text.toString().toLong()
            val description = descriptionEditText.text.toString()
            // Get the current date
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = dateFormat.format(calendar.time)
            val itemdata = InventoryDataClass(0,itemName, quantity, sellingprice, description,date)
            viewModel.insertInventoryitem(itemdata)


            recyclerViewAdapter.notifyDataSetChanged() // Add this line

            clearinputs()
        }


        backbutton.setOnClickListener {
            inputFields.visibility = View.GONE
        }


        return view

    }


    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun clearinputs(){
        // Reset the input fields
        itemNameEditText.setText(resources.getString(R.string.empty_string))
        quantityEditText.setText(resources.getString(R.string.empty_string))
        sellingpriceEditText.setText(resources.getString(R.string.empty_string))
        descriptionEditText.setText(resources.getString(R.string.empty_string))

    }


    fun removeItem(itemName: String) {

        viewModel.deleteInventoryitem(itemName)
    }

    fun updateItemQuantity(itemName: String, newQuantity: Int) {
        viewModel.updateInventoryitem(itemName, newQuantity)
    }


}



































/*
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

 */