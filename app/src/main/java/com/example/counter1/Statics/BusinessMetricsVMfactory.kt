package com.example.counter1.Statics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.counter1.Db.SalesDao

class BusinessMetricsVMfactory(private val dao: SalesDao) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusinessMetricsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusinessMetricsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}