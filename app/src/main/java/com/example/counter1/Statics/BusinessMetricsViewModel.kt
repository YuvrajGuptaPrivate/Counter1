package com.example.counter1.Statics

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.counter1.Db.InventoryDao
import com.example.counter1.Db.InventoryDataClass
import com.example.counter1.Db.Order
import com.example.counter1.Db.OrderDao
import com.example.counter1.Db.Sales
import com.example.counter1.Db.SalesDao

class BusinessMetricsViewModel (private val dao : SalesDao):ViewModel() {


    val AllSalesData : LiveData<List<Sales>> = dao.GetAllSAles()


    
}