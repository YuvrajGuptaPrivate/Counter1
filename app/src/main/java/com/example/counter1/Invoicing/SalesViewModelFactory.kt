package com.example.counter1.Invoicing
import com.example.counter1.Db.SalesDao
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel

class SalesViewModelFactory(private val salesDao: SalesDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SalesViewModel::class.java)) {
            return SalesViewModel(salesDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}