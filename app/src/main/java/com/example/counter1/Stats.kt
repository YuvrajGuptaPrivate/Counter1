package com.example.counter1

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        val businessMetrics = BusinessMetrics(requireContext())
        // Calculate and update Total Expenses TextView
        val totalExpenses = businessMetrics.calculateExpensesFromInventoryFile()
        val totalExpensesTextView = view.findViewById<TextView>(R.id.totalexpenssestextview)
        totalExpensesTextView.text = "Total Expenses: Rs ${totalExpenses}"

        // Calculate and update Total Revenue TextView
        val totalRevenue = businessMetrics.calculateRevenue()
        val totalRevenueTextView = view.findViewById<TextView>(R.id.totalrevenuetextview)
        totalRevenueTextView.text = "Total Revenue: Rs ${totalRevenue}"


        // Calculate and update Net Profit TextView
        val netProfit = businessMetrics.calculateIncome(businessMetrics.calculateExpensesFromInventoryFile())
        val netProfitTextView = view.findViewById<TextView>(R.id.Netprofittextview)
        netProfitTextView.text = "Net Profit: Rs ${netProfit}"












        val pieChart = view?.findViewById<PieChart>(R.id.pie_chart)
        // Create a pie chart data set
        val pieDataSet = PieDataSet(listOf(
            PieEntry(businessMetrics.calculateSalaryExpenses().toFloat(), "Salary"),
            PieEntry(businessMetrics.calculateMarketingExpenses().toFloat(), "Marketing"),
            PieEntry(businessMetrics.calculateRentExpenses().toFloat(), "Rent"),
            PieEntry(businessMetrics.calculateOtherExpenses().toFloat(), "Other")
        ), "")

        // Set the colors for the pie chart slices
        pieDataSet.colors = listOf(
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






        //Bar chat for revenue

        val revenueChart = view.findViewById<BarChart>(R.id.RevenueBarchart)
        val revenueData = ArrayList<BarEntry>()

         // Calculate and add data to the revenueData list
        revenueData.add(BarEntry(20f, businessMetrics.calculateRevenue().toFloat())) // Day 2: Revenue

        val barDataSet = BarDataSet(revenueData, "Revenue")
        barDataSet.color = Color.rgb(255, 165, 0) // Orange color

        val barData = BarData(listOf(barDataSet))
        revenueChart.data = barData
        revenueChart.animateY(2000) // Animate the chart



        // Line chart

        val pLData = ArrayList<Entry>()

// Calculate and add data to the pLData list
        pLData.add(Entry(10f, businessMetrics.calculateIncome(businessMetrics.calculateExpensesFromInventoryFile()).toFloat())) // Day 1: Profit/Loss

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





        return view
    }
}