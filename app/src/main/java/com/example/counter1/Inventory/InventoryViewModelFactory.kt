package com.example.counter1.Inventory

import androidx.lifecycle.ViewModelProvider.Factory
import com.example.counter1.Db.InventoryDao
import androidx.lifecycle.ViewModel

class InventoryViewModelFactory(private val dao:InventoryDao): Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}