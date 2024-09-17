package com.example.counter1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast


class home : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val businessMetrics = BusinessMetrics(requireContext())
        // Calculate and update Total Expenses TextView
        val totalExpenses = businessMetrics.calculateExpenses()
        val totalExpensesTextView = view.findViewById<TextView>(R.id.expensestextview)
        totalExpensesTextView.text = "Rs ${totalExpenses}"

        // Calculate and update Total Revenue TextView
        val totalRevenue = businessMetrics.calculateRevenue()
        val totalRevenueTextView = view.findViewById<TextView>(R.id.revenuetextview)
        totalRevenueTextView.text = "Rs ${totalRevenue}"


        // Calculate and update Net Profit TextView
        val netProfit = businessMetrics.calculateIncome(businessMetrics.calculateExpensesFromInventoryFile())
        val netProfitTextView = view.findViewById<TextView>(R.id.profittextview)
        netProfitTextView.text = "Rs ${netProfit}"


        val editInvoiceButton = view.findViewById<ImageView>(R.id.setting)
        editInvoiceButton.setOnClickListener {
            val intent = Intent(requireContext(), Inputs::class.java)
            startActivity(intent)
        }

        val billingbutton = view.findViewById<LinearLayout>(R.id.billingbuton)
        billingbutton.setOnClickListener{
            (requireActivity() as MainActivity).replaceFragments(Billing())
        }
        val inventorybutton = view.findViewById<LinearLayout>(R.id.InventoryManage)
        inventorybutton.setOnClickListener{
            (requireActivity() as MainActivity).replaceFragments(Inventory())
        }
        val profitlossbutton = view.findViewById<LinearLayout>(R.id.profitlosss)
        profitlossbutton.setOnClickListener{
            (requireActivity() as MainActivity).replaceFragments(Stats())

        }

        val shopinfobutton = view.findViewById<LinearLayout>(R.id.Shopinfobutton)
        shopinfobutton.setOnClickListener{
            val intent = Intent(requireContext(),Inputs::class.java)
            startActivity(intent)
        }




        return view
    }
}
