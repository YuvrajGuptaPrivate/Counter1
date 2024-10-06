package com.example.counter1.Invoicing

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.counter1.R
import java.io.File

class InvoiceAdapter(private val invoices: List<Invoice>, private val openInvoiceDetail: (Invoice) -> Unit) :
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
                openInvoiceDetail(invoice)

            }
        }
        private fun openInvoiceDetail(invoice: Invoice) {
            val context = itemView.context
            val pdfFile = File(context.filesDir, "${invoice.clientName}-INV-${invoice.id}-${invoice.date}.pdf") // Construct the file path

            // Check if the file exists
            if (pdfFile.exists()) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf")
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

                // Check if there is an app available to handle the PDF
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "No application available to open PDF", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(context, "Invoice PDF not found", Toast.LENGTH_SHORT).show()
            }
        }
    }


}