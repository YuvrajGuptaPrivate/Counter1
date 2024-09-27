package com.example.counter1.Invoicing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.counter1.Db.Order
import com.example.counter1.Db.OrderDao
import kotlinx.coroutines.launch

class OrderViewModel(private val orderDao: OrderDao): ViewModel() {

    val allorderData: LiveData<List<Order>> = orderDao.GetALLOrderData()

    fun insertorderData(order: Order) = viewModelScope.launch {
        orderDao.InsertOrderData(order)
    }

    fun deleteorderData(order: Order) = viewModelScope.launch {
        orderDao.DeleteOrderData(order)
    }

}