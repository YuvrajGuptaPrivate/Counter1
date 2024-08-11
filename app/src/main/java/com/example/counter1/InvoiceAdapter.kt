package com.example.counter1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InvoiceAdapter(private val invoices: List<Invoice>, private val onClick: (Invoice) -> Unit) :
    RecyclerView.Adapter<InvoiceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invoices, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val invoice = invoices[position]
        holder.bind(invoice)
    }

    override fun getItemCount(): Int = invoices.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val invoiceNumber: TextView = itemView.findViewById(R.id.invoice_number)
        private val clientName: TextView = itemView.findViewById(R.id.client_name)
        private val date: TextView = itemView.findViewById(R.id.date)
        private val status: TextView = itemView.findViewById(R.id.status)

        fun bind(invoice: Invoice) {
            invoiceNumber.text = "Invoice #${invoice.id}"
            clientName.text = "Client Name: ${invoice.clientName}"
            date.text = "Date: ${invoice.date}"
            status.text = if (invoice.isPaid) "Status: Paid" else "Status: Unpaid"

            itemView.setOnClickListener {
                onClick(invoice)
            }
        }
    }
}