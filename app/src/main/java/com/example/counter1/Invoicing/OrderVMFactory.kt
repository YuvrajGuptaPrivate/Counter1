package com.example.counter1.Invoicing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.counter1.Db.OrderDao

class OrderVMFactory(private val orderDao: OrderDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
                return OrderViewModel(orderDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

}