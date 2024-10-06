package com.example.counter1.Invoicing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.example.counter1.Db.InventoryDao

class InvoiceScreenViewFactory(private val dao: InventoryDao): Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InvoiceScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InvoiceScreenViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
