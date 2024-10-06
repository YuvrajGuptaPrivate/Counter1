package com.example.counter1.Db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

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

@Entity(
    foreignKeys = [ForeignKey(
        entity = InventoryDataClass::class,
        parentColumns = arrayOf("itemId"),
        childColumns = arrayOf("itemId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Sales(
    @PrimaryKey
    @ColumnInfo(name = "itemId")
    val itemId: Int,
    @ColumnInfo(name = "itemprice")
    var itemprice: Long,
    @ColumnInfo(name = "quantity")
    var quantity : Int
)

data class InventoryAndSales(
    @Embedded
    val inventoryDataClass: InventoryDataClass,
    @Relation(
        parentColumn = "itemId",
        entityColumn = "itemId"
    )
    val sales: List<Sales>
)




@Entity("ORDERDATA")
data class Order(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("orderid")
    val orderid : Int,
    @ColumnInfo(name = "CustomerName")
    val customerName:String,
    @ColumnInfo(name = "ItemsOrdered")
    val itemsOrdered:String,
    @ColumnInfo(name = "TotalCartValue")
    val TotalCartValue:Long
)
