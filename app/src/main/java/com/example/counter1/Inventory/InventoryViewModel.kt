package com.example.counter1.Inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.counter1.Db.InventoryDao
import com.example.counter1.Db.InventoryDataClass
import kotlinx.coroutines.launch

class InventoryViewModel(private val dao: InventoryDao):ViewModel() {


    val AllInventory : LiveData<List<InventoryDataClass>> = dao.GetALLInventory()


    fun insertInventoryitem(item: InventoryDataClass)=viewModelScope.launch {
        dao.InsertItem(item)
    }

    fun deleteInventoryitem(itemName: String)=viewModelScope.launch {
        dao.DeleteItemByName(itemName)
    }

    fun updateInventoryitem(itemName: String, newQuantity : Int)=viewModelScope.launch {
        dao.UpdateItemQuantity(itemName,newQuantity)
    }


}