package com.example.counter1.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.counter1.Db.AppDATABASE
import com.example.counter1.Db.InventoryDao
import com.example.counter1.Db.InventoryDataClass
import com.example.counter1.Db.Sales
import com.example.counter1.Db.SalesDao
import com.example.counter1.Utility.Inputs
import com.example.counter1.Inventory.Inventory
import com.example.counter1.Inventory.InventoryViewModel
import com.example.counter1.Inventory.InventoryViewModelFactory
import com.example.counter1.Invoicing.Billing
import com.example.counter1.MainActivity
import com.example.counter1.R
import com.example.counter1.Statics.BusinessMetricsVMfactory
import com.example.counter1.Statics.BusinessMetricsViewModel
import com.example.counter1.Statics.Stats


class home : Fragment() {
    private lateinit var viewModel: BusinessMetricsViewModel
    private lateinit var dao: SalesDao
    private lateinit var inventoryDao: InventoryDao
    private lateinit var inventoryViewModel: InventoryViewModel

    private  var totalsales : Long = 0
    private var  totalInventoryCost : Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        dao = AppDATABASE.getinstance(requireContext()).salesDao()
        val factory = BusinessMetricsVMfactory(dao)
        viewModel = ViewModelProvider(this, factory).get(BusinessMetricsViewModel::class.java)




        inventoryDao = AppDATABASE.getinstance(requireContext()).inventoryDao()
        val inventoryfactory = InventoryViewModelFactory(inventoryDao)
        inventoryViewModel =
            ViewModelProvider(this, inventoryfactory).get(InventoryViewModel::class.java)


        viewModel.AllSalesData.observe(viewLifecycleOwner, Observer { salesList->
        inventoryViewModel.AllInventory.observe(viewLifecycleOwner, Observer { inventoryList ->
            totalInventoryCost = calculateInventoryTotalVAlue(inventoryList)
            totalsales = calculateTotalSalesAmount(salesList)



            fun calculateSale(): Long {
                return totalsales
            }

            // Calculate Revenue
            fun calculateRevenue(): Long {
                val otherIncome = 0
                val sales = calculateSale()
                return sales + otherIncome
            }
            // Calculate Income
            fun calculateIncome(): Double {
                val revenue = calculateRevenue()
                return revenue*0.4
            }



            // Calculate and update Total Revenue TextView
            val totalRevenue = calculateRevenue().toString()
            val totalRevenueTextView = view.findViewById<TextView>(R.id.revenuetextview)
            totalRevenueTextView.text = "Rs ${totalRevenue}"

            // Calculate and update Total Expenses TextView
            val totalExpenses = calculateExpenses(totalInventoryCost).toLong()
            val toalE = totalExpenses.toString()
            val totalExpensesTextView = view.findViewById<TextView>(R.id.expensestextview)
            totalExpensesTextView.text = "Rs ${toalE}"


            // Calculate and update Net Profit TextView
            val netProfit = calculateIncome().toString()
            val netProfitTextView = view.findViewById<TextView>(R.id.profittextview)
            netProfitTextView.text = "Rs ${netProfit}"


        })
    })





        val editInvoiceButton = view.findViewById<ImageView>(R.id.setting)
        editInvoiceButton.setOnClickListener {
            val intent = Intent(requireContext(), Inputs::class.java)
            startActivity(intent)
        }

        val billingbutton = view.findViewById<LinearLayout>(R.id.bll)
        billingbutton.setOnClickListener{
            (requireActivity() as MainActivity).replaceFragments(Billing())
        }
        val inventorybutton = view.findViewById<LinearLayout>(R.id.imll)
        inventorybutton.setOnClickListener{
            (requireActivity() as MainActivity).replaceFragments(Inventory())
        }
        val profitlossbutton = view.findViewById<LinearLayout>(R.id.palll)
        profitlossbutton.setOnClickListener{
            (requireActivity() as MainActivity).replaceFragments(Stats())

        }

        val shopinfobutton = view.findViewById<LinearLayout>(R.id.Shopinfobutton)
        shopinfobutton.setOnClickListener{
            val intent = Intent(requireContext(), Inputs::class.java)
            startActivity(intent)
        }




        return view
    }



    fun calculateTotalSalesAmount(salesList: List<Sales>): Long {
        return salesList.sumOf { sale -> sale.quantity * sale.itemprice }
    }


    fun calculateInventoryTotalVAlue(inventoryList: List<InventoryDataClass>): Long {
        return inventoryList.sumOf { cost -> cost.itemstocks * cost.itemprice }
    }


    // Calculate Expenses
    fun calculateExpenses(expense:Long): Double {
        val totalinventorycost = expense*0.7
        val others = expense*0.3
        val salarys= expense*0.1
        val marketingcost= expense*0.1
        val Rents = expense*0.1

        return totalinventorycost + others + salarys + marketingcost + Rents
    }



}
