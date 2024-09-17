package com.example.counter1.Db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Inventory")
data class InventoryDataClass(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId")
    val itemId : Int,
    @ColumnInfo(name = "itemName")
    val itemName: String,
    @ColumnInfo(name = "itemstocks")
    var itemstocks : Int,
    @ColumnInfo(name = "itemprice")
    var itemprice:Long,
    @ColumnInfo(name = "itemdescription")
    var itemdescription: String,
    @ColumnInfo(name = "itemdate")
    var itemdate :String)



@Entity("Sales")
data class Sales(
    @ColumnInfo(name = "itemId")
    val itemId: Int,
    @ColumnInfo(name = "itemprice")
    var itemprice: Long,
    @ColumnInfo(name = "quantity")
    var quantity : Int
)

@Entity("ORDERDATA")
data class Order(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("orderid ")
    val orderid : Int,
    @ColumnInfo(name = "CustomerName")
    val customerName:String,
    @ColumnInfo(name = "ItemsOrdered")
    val itemsOrdered:String,
    @ColumnInfo(name = "TotalCartValue")
    val TotalCartValue:Long
)
