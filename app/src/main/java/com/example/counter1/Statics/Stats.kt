package com.example.counter1.Statics

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.counter1.Db.AppDATABASE
import com.example.counter1.Db.InventoryDao
import com.example.counter1.Db.InventoryDataClass
import com.example.counter1.Db.Sales
import com.example.counter1.Db.SalesDao
import com.example.counter1.Inventory.InventoryViewModel
import com.example.counter1.Inventory.InventoryViewModelFactory
import com.example.counter1.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class Stats : Fragment() {
    private lateinit var viewModel: BusinessMetricsViewModel
    private lateinit var dao: SalesDao
    private lateinit var inventoryDao: InventoryDao
    private lateinit var inventoryViewModel: InventoryViewModel

    private var  totalInventoryCost : Long = 0
    private  var totalsales : Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)


        dao = AppDATABASE.getinstance(requireContext()).salesDao()
        val factory = BusinessMetricsVMfactory(dao)
        viewModel = ViewModelProvider(this,factory).get(BusinessMetricsViewModel::class.java)

        viewModel.AllSalesData.observe(viewLifecycleOwner, Observer { salesList->
              totalsales = calculateTotalSalesAmount(salesList)


            fun calculateSale():Double  {
                return totalsales.toDouble()
            }

            // Calculate Revenue
            fun calculateRevenue(): Double {
                val otherIncome = 0
                val sales = calculateSale()
                return sales + otherIncome
            }

            // Calculate Income
            fun calculateIncome(): Double {
                val revenue = calculateRevenue()
                return revenue*0.4
            }



            // Calculate and update Net Profit TextView
            val netProfit = calculateIncome()
            val netProfitTextView = view.findViewById<TextView>(R.id.Netprofittextview)
            netProfitTextView.text = "Net Profit: Rs ${netProfit}"

            // Calculate and update Total Revenue TextView
            val totalRevenue = calculateRevenue()
            val totalRevenueTextView = view.findViewById<TextView>(R.id.totalrevenuetextview)
            totalRevenueTextView.text = "Total Revenue: Rs ${totalRevenue}"





            //Bar chat for revenue

            val revenueChart = view.findViewById<BarChart>(R.id.RevenueBarchart)
            val revenueData = ArrayList<BarEntry>()

            // Calculate and add data to the revenueData list
            revenueData.add(BarEntry(20f, calculateRevenue().toFloat())) // Day 2: Revenue

            val barDataSet = BarDataSet(revenueData, "Revenue")
            barDataSet.color = Color.rgb(255, 165, 0) // Orange color

            val barData = BarData(listOf(barDataSet))
            revenueChart.data = barData
            revenueChart.animateY(2000) // Animate the chart





            val pLData = ArrayList<Entry>()

// Calculate and add data to the pLData list
            pLData.add(Entry(10f, calculateIncome().toFloat())) // Day 1: Profit/Loss

            val pLDataSet = LineDataSet(pLData, "P&L")
            pLDataSet.color = Color.rgb(255, 165, 0) // Orange color
            pLDataSet.setCircleColor(Color.rgb(255, 165, 0)) // Orange color for circle
            pLDataSet.lineWidth = 2f
            pLDataSet.circleRadius = 4f
            pLDataSet.setDrawCircles(true)
            pLDataSet.setDrawValues(true)

            val pLDataList = listOf(pLDataSet)
            val pLChart = view.findViewById<LineChart>(R.id.linechartProfitnloss)
            pLChart.data = LineData(pLDataList)
            pLChart.animateY(2000) // Animate the chart

// Customize the chart appearance
            pLChart.description.text = "Profit and Loss"
            pLChart.legend.isEnabled = true
            pLChart.axisLeft.isEnabled = true
            pLChart.axisRight.isEnabled = true
            pLChart.xAxis.isEnabled = true



        })


















        inventoryDao = AppDATABASE.getinstance(requireContext()).inventoryDao()
        val inventoryfactory = InventoryViewModelFactory(inventoryDao)
        inventoryViewModel = ViewModelProvider(this,inventoryfactory).get(InventoryViewModel::class.java)


        inventoryViewModel.AllInventory.observe(viewLifecycleOwner, Observer {
            inventoryList->
            totalInventoryCost = calculateInventoryTotalVAlue(inventoryList)

            // Calculate and update Total Expenses TextView
            val totalExpenses = calculateExpenses(totalInventoryCost)

            val totalExpensesTextView = view.findViewById<TextView>(R.id.totalexpenssestextview)
            totalExpensesTextView.text = "Total Expenses: Rs ${totalExpenses}"



            val pieChart = view?.findViewById<PieChart>(R.id.pie_chart)
            // Create a pie chart data set
            val pieDataSet = PieDataSet(listOf(
                PieEntry((totalInventoryCost*0.7).toFloat(),"Inventory Cost"),
                PieEntry((totalInventoryCost*0.2).toFloat(), "Salary"),
                PieEntry((totalInventoryCost*0.1).toFloat(), "Marketing"),
                PieEntry((totalInventoryCost*0.1).toFloat(), "Rent"),
                PieEntry((totalInventoryCost*0.3).toFloat(), "Other")
            ), "")

            // Set the colors for the pie chart slices
            pieDataSet.colors = listOf(
                Color.rgb(255,165,0),
                Color.rgb(255, 0, 0), // Red for Salary
                Color.rgb(0, 255, 0), // Green for Marketing
                Color.rgb(0, 0, 255), // Blue for Rent
                Color.rgb(255, 255, 0) // Yellow for Other
            )

            // Create a pie chart data object
            val pieData = PieData(pieDataSet)

            // Set the pie chart data
            pieChart?.data = pieData

            // Customize the pie chart appearance
            pieChart?.centerText = "Expenses"
            pieChart?.setCenterTextSize(20f)
            pieChart?.legend?.isEnabled = false

            // Enable interaction and display the chart
            pieChart?.isClickable = true
            pieChart?.setUsePercentValues(true)
            pieChart?.description?.isEnabled = false

            // Notify the chart that the data has changed
            pieChart?.notifyDataSetChanged()
            pieChart?.invalidate()


        })


        return view
    }







    fun calculateTotalSalesAmount(salesList: List<Sales>): Long {
        return salesList.sumOf { sale -> sale.quantity * sale.itemprice }
    }


    fun calculateInventoryTotalVAlue(inventoryList: List<InventoryDataClass>): Long {
        return inventoryList.sumOf { cost -> cost.itemstocks * cost.itemprice }
    }


    // Calculate Expenses
    fun calculateExpenses(totalinventorycost:Long): Double {
        val totalExpenses = totalinventorycost*0.7
        val others = totalinventorycost*0.3
        val salarys= totalinventorycost*0.1
        val marketingcost= totalinventorycost*0.1
        val Rents = totalinventorycost*0.1

        return totalExpenses + others + salarys + marketingcost + Rents
    }



}