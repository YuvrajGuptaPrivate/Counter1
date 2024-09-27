package com.example.counter1.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [InventoryDataClass::class,Sales::class,Order::class], version = 4, exportSchema =false )
abstract class AppDATABASE :RoomDatabase(){

    abstract fun inventoryDao() : InventoryDao

    abstract fun salesDao():SalesDao

    abstract fun orderDao():OrderDao


    companion object{
        @Volatile
        private var Instance : AppDATABASE?=null
        fun getinstance(context: Context):AppDATABASE{
            synchronized(this){
                var instance = Instance
                if (instance ==null){
                    instance=Room.databaseBuilder(
                        context.applicationContext,AppDATABASE::class.java,"DATABASE"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }

}