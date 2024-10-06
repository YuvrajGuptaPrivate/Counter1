package com.example.counter1.Db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface InventoryDao {
    @Insert
    suspend fun InsertItem(inventoryDataClass: InventoryDataClass)

    @Query("DELETE FROM Inventory WHERE itemName = :itemName")
    suspend fun DeleteItemByName(itemName: String)


    @Query("UPDATE Inventory SET itemstocks = :newQuantity WHERE itemName = :itemName")
    suspend fun UpdateItemQuantity(itemName: String, newQuantity: Int)

    @Query ("SELECT * FROM Inventory")
      fun GetALLInventory():LiveData<List<InventoryDataClass>>
}



@Dao
interface SalesDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insertSale(sales: Sales)

    @Query("SELECT * FROM Sales WHERE itemId = :itemId")
    fun getSaleByItemId(itemId: Int): Sales?

    @Delete
    suspend fun DeleteSalesData(sales: Sales)

    @Update
    suspend fun UpdateSalesData(sales: Sales)

    @Query("SELECT * FROM Sales")
     fun GetAllSAles():LiveData<List<Sales>>

    @Transaction
   suspend fun insertOrUpdateSale(sales: Sales) {
        val existingSale = getSaleByItemId(sales.itemId)
        if (existingSale != null) {
            val updatedSale = Sales(
                itemId = sales.itemId,
                itemprice = sales.itemprice,
                quantity = existingSale.quantity + sales.quantity
            )
            UpdateSalesData(updatedSale)
        } else {
            insertSale(sales)
        }
    }

}


@Dao
interface OrderDao{
    @Insert
    suspend fun InsertOrderData(order: Order)

    @Delete
    suspend fun DeleteOrderData(order: Order)

    @Query("SELECT * FROM ORDERDATA")
     fun GetALLOrderData():LiveData<List<Order>>
}