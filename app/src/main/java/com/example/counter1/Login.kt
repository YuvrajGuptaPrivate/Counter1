package com.example.counter1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val textView = findViewById<TextView>(R.id.bottomtext)
        val button= findViewById<Button>(R.id.Loginbutton)

        textView.setOnClickListener{
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }
        button.setOnClickListener {
            val sharedPreferences = getSharedPreferences("inputs_data", MODE_PRIVATE)

            val companyName = sharedPreferences.getString("company_name", "")
            val companyAddress = sharedPreferences.getString("company_address", "")
            val gstNumber = sharedPreferences.getString("gst_number", "")
            val phoneNumber = sharedPreferences.getString("phone_number", "")
            val emailId = sharedPreferences.getString("email_id", "")

            if (companyName!!.isNotEmpty() && companyAddress!!.isNotEmpty() && gstNumber!!.isNotEmpty() && phoneNumber!!.isNotEmpty() && emailId!!.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, Inputs::class.java)
                startActivity(intent)
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}