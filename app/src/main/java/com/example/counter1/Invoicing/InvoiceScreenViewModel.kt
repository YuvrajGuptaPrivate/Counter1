package com.example.counter1.Invoicing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.counter1.Db.InventoryDao
import com.example.counter1.Db.InventoryDataClass
import kotlinx.coroutines.launch

class InvoiceScreenViewModel(private val dao: InventoryDao): ViewModel() {


    val AllInventory : LiveData<List<InventoryDataClass>> = dao.GetALLInventory()




    fun updateInventoryitem(itemName: String, newQuantity : Int)=viewModelScope.launch {
        dao.UpdateItemQuantity(itemName,newQuantity)
    }


}