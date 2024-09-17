package com.example.counter1.Db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface InventoryDao {
    @Insert
    suspend fun InsertItem(inventoryDataClass: InventoryDataClass)

    @Delete
    suspend fun DeleteItem(inventoryDataClass: InventoryDataClass)

    @Update
    suspend fun UpdateItem(inventoryDataClass: InventoryDataClass)

    @Query ("SELECT * FROM Inventory")
    suspend fun GetALLInventory():LiveData<List<InventoryDataClass>>
}



@Dao
interface SalesDao{
    @Insert
    suspend fun InsertSalesData(sales: Sales)

    @Delete
    suspend fun DeleteSalesData(sales: Sales)

    @Update
    suspend fun UpdateSalesData(sales: Sales)

    @Query("SELECT * FROM Sales")
    suspend fun GetAllSAles():LiveData<List<Sales>>
}


@Dao
interface OrderDao{
    @Insert
    suspend fun InsertOrderData(order: Order)

    @Delete
    suspend fun DeleteOrderData(order: Order)

    @Query("SELECT * FROM ORDERDATA")
    suspend fun GetALLOrderData():LiveData<List<Order>>
}