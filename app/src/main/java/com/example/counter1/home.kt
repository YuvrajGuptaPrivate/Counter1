package com.example.counter1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


class home : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val editInvoiceButton = view.findViewById<ImageView>(R.id.setting)
        editInvoiceButton.setOnClickListener {
            val intent = Intent(requireContext(), Inputs::class.java)
            startActivity(intent)
        }

        return view
    }
}
