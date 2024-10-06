package com.example.counter1.Invoicing

import androidx.lifecycle.LiveData
import com.example.counter1.Db.Sales
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.counter1.Db.SalesDao
import kotlinx.coroutines.launch

class SalesViewModel(private val salesDao: SalesDao) : ViewModel() {


    val allSales: LiveData<List<Sales>> = salesDao.GetAllSAles()

    fun insertSalesData(sales: Sales) = viewModelScope.launch {
        salesDao.insertOrUpdateSale(sales)
    }

    fun deleteSalesData(sales: Sales) = viewModelScope.launch {
        salesDao.DeleteSalesData(sales)
    }

    fun updateSalesData(sales: Sales) = viewModelScope.launch {
        salesDao.UpdateSalesData(sales)
    }
}