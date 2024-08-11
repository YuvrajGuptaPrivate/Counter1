package com.example.counter1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Billing : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InvoiceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_billing, container, false)

        recyclerView = view.findViewById(R.id.InvoiceRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val invoices = listOf(
            Invoice(1, "John Doe", "2022-01-01", true),
            Invoice(2, "Jane Doe", "2022-01-02", false),
            Invoice(3, "Bob Smith", "2022-01-03", true),
        )

        adapter = InvoiceAdapter(invoices) { invoice ->
            Toast.makeText(context, "Clicked on invoice ${invoice.id}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter

        return view
    }
}

data class Invoice(val id: Int, val clientName: String, val date: String, val isPaid: Boolean)



