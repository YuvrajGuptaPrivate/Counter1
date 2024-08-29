package com.example.counter1
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Button
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Intro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)
        val button = findViewById<Button>(R.id.btnGetStart)
        val logoImageView = findViewById<ImageView>(R.id.imageView2)

        if (isNightModeEnabled(this)) {
            logoImageView.setImageResource(R.drawable.nightlogo)
        } else {
            logoImageView.setImageResource(R.drawable.logosvg)
        }

        // Set the OnClickListener
        button.setOnClickListener {
            // Handle the button click here
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
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
