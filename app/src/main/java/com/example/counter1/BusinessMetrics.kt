package com.example.counter1

import android.content.Context
import com.example.counter1.Invoicing.Item
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class BusinessMetrics(private val context: Context) {
    // Calculate Sale
    fun calculateSale(): Double {
        val file = File(context.filesDir, "sold_items.txt")
        if (!file.exists()) {
            // Create the file if it doesn't exist
            file.createNewFile()
        }
        var totalSale = 0.0
        file.forEachLine { line ->
            val values = line.split(",")
            if (values.size == 3) {
                val quantity = values[1].toInt()
                val price = values[2].toDouble()
                totalSale += quantity * price
            }
        }
        return totalSale
    }

    // Calculate Revenue
    fun calculateRevenue(): Double {
        val otherIncome = 0
        val sales = calculateSale()
        return sales + otherIncome
    }

    // Calculate Income
    fun calculateIncome(expenses: Double): Double {
        val revenue = calculateRevenue()
        return revenue - expenses
    }

    // Calculate Expenses
    fun calculateExpenses(): Long {
        val totalExpenses = calculateExpensesFromInventoryFile().toLong()
        val others = calculateOtherExpenses().toLong()
        val salarys= calculateSalaryExpenses().toLong()
        val marketingcost= calculateMarketingExpenses().toLong()
        val Rents =calculateRentExpenses().toLong()

        return totalExpenses + others + salarys + marketingcost + Rents
    }






    // Function to calculate expenses from inventory data
    fun calculateExpensesFromInventory(data: ArrayList<Item>): Double {
        var totalSellingPrice = 0.0

        // Calculate total selling price
        for (item in data) {
            totalSellingPrice += (item.sellingprice).toDouble() * item.quantity.toDouble()

        }

        // Assume cost of goods sold is 50% of total selling price
        val totalinventorycost = totalSellingPrice





        // Calculate expenses
        return totalinventorycost
    }

    // Usage
    fun calculateExpensesFromInventoryFile(): Double {
        val data = loadInventoryData()
        return calculateExpensesFromInventory(data)
    }
    fun loadInventoryData(): ArrayList<Item> {
        val file = File(context.filesDir, "inventory_data.txt")
        val data = ArrayList<Item>()

        if (file.exists()) {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                val parts = line!!.split(",")
                data.add(Item(parts[0], parts[1], parts[2], parts[3]))
            }

            bufferedReader.close()
        }

        return data
    }

    // Function to calculate salary expenses
    fun calculateSalaryExpenses(): Double {
        // Assume salary expenses are 20% of total expenses
        val totalExpenses = calculateExpensesFromInventoryFile()
        return totalExpenses * 0.2

    }

    fun calculateInventoryCostExpenses(): Double {
        val totalExpenses = calculateExpensesFromInventoryFile()
        return totalExpenses * 0.7

    }

    // Function to calculate marketing expenses
    fun calculateMarketingExpenses(): Double {
        val totalExpenses = calculateExpensesFromInventoryFile()
        return  totalExpenses *0.1
    }

    // Function to calculate rent expenses
    fun calculateRentExpenses(): Double {
        // Assume rent expenses are 10% of total expenses
        val totalExpenses = calculateExpensesFromInventoryFile()
        return  totalExpenses *0.1

    }

    // Function to calculate other expenses
    fun calculateOtherExpenses(): Double {
        val totalExpenses = calculateExpensesFromInventoryFile()
        return totalExpenses * 0.03

    }


}