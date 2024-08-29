package com.example.counter1

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        //piechart
        val pieChart = view?.findViewById<PieChart>(R.id.pie_chart)

        // Create a pie chart data set
        val pieDataSet = PieDataSet(listOf(
            PieEntry(20f, "Revenue"),
            PieEntry(30f, "Expenses"),
            PieEntry(50f, "Profit")
        ), "Profit and Loss")

        // Set the colors for the pie chart slices
        pieDataSet.colors = listOf(
            Color.rgb(255, 0, 0), // Red for Revenue
            Color.rgb(0, 255, 0), // Green for Expenses
            Color.rgb(0, 0, 255) // Blue for Profit
        )

        // Create a pie chart data object
        val pieData = PieData(pieDataSet)

        // Set the pie chart data
        pieChart?.data = pieData

        // Customize the pie chart appearance
        pieChart?.centerText = "Profit and Loss"
        pieChart?.setCenterTextSize(24f)
        pieChart?.legend?.isEnabled = false

        // Enable interaction and display the chart
        pieChart?.isClickable = true
        pieChart?.setUsePercentValues(true)
        pieChart?.description?.isEnabled = false

        // Notify the chart that the data has changed
        pieChart?.notifyDataSetChanged()
        pieChart?.invalidate()



       val revenueChart = view.findViewById<BarChart>(R.id.RevenueBarchart)
        val revenueData = ArrayList<BarEntry>()

           // Add data to the revenueData list
        revenueData.add(BarEntry(10f, 5000f)) // Day 1: $5000
        revenueData.add(BarEntry(20f, 6000f)) // Day 2: $6000
        revenueData.add(BarEntry(30f, 7000f)) // Day 3: $7000

        val barDataSet = BarDataSet(revenueData, "Revenue")
        barDataSet.color = Color.rgb(255, 165, 0) // Orange color

        val barData = BarData(listOf(barDataSet))
        revenueChart.data = barData
        revenueChart.animateY(2000) // Animate the chart

        //line chart

        val pLData = ArrayList<Entry>()
        pLData.add(Entry(10f, 5000f)) // Profit: $5000
        pLData.add(Entry(20f, -2000f)) // Loss: -$2000
        pLData.add(Entry(30f, 3000f)) // Profit: $3000
// ...

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
        pLChart.description.text = "Profit and Loss Chart"
        pLChart.legend.isEnabled = true
        pLChart.axisLeft.isEnabled = true
        pLChart.axisRight.isEnabled = true
        pLChart.xAxis.isEnabled = true


        return view
    }
}