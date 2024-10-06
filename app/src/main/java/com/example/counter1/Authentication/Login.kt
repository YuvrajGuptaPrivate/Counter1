package com.example.counter1.Authentication

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.counter1.Utility.Inputs
import com.example.counter1.MainActivity
import com.example.counter1.R

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val textView = findViewById<TextView>(R.id.bottomtext)
        val button= findViewById<Button>(R.id.Loginbutton)
        val logoImageView = findViewById<ImageView>(R.id.toptext)

        if (isNightModeEnabled(this)) {
            logoImageView.setImageResource(R.drawable.nightlogo)
        } else {
            logoImageView.setImageResource(R.drawable.logosvg)
        }

        textView.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
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
                Toast.makeText(this, "Please give inputs first", Toast.LENGTH_LONG).show()
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun isNightModeEnabled(context: Context): Boolean {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}