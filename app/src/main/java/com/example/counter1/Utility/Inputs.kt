package com.example.counter1.Utility

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.counter1.MainActivity
import com.example.counter1.R

enum class ButtonState {
    NEXT, SAVE
}

enum class ViewState {
    STEP1, STEP2
}


class Inputs : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var companynameinput: EditText
    private lateinit var companyaddressinput: EditText
    private lateinit var beneficiarynameinput: EditText
    private lateinit var bankifsccodeinput: EditText
    private lateinit var accountnumberinput: EditText
    private lateinit var banknameinput: EditText
    private lateinit var upiidinput: EditText
    private lateinit var gstnumberinput: EditText
    private lateinit var phonenumberinput: EditText
    private lateinit var emailinput: EditText
    private lateinit var saveinputbutton: Button
    private var buttonState = ButtonState.NEXT
    private var viewState = ViewState.STEP1



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inputs)

        sharedPreferences = getSharedPreferences("inputs_data", MODE_PRIVATE)

        companynameinput = findViewById(R.id.companyname_input)
        companyaddressinput = findViewById(R.id.companyaddress_input)
        beneficiarynameinput = findViewById(R.id.beneficiaryname_input)
        bankifsccodeinput = findViewById(R.id.bankifsccode_input)
        accountnumberinput = findViewById(R.id.accountnumber_input)
        banknameinput = findViewById(R.id.bankname_input)
        upiidinput = findViewById(R.id.upiid_input)
        gstnumberinput = findViewById(R.id.gstnumber_input)
        phonenumberinput = findViewById(R.id.phonenumber_input)
        emailinput = findViewById(R.id.email_input)
        saveinputbutton = findViewById(R.id.save_input_button)


        val editTextGroup1 = findViewById<LinearLayout>(R.id.editTextGroup1)
        val editTextGroup2 = findViewById<LinearLayout>(R.id.editTextGroup2)



        saveinputbutton.setOnClickListener {
            when (buttonState) {
                ButtonState.NEXT -> {
                    // Toggle the visibility of the two groups
                    editTextGroup1.visibility = View.GONE
                    editTextGroup2.visibility = View.VISIBLE
                    buttonState = ButtonState.SAVE
                    viewState = ViewState.STEP2
                }
                ButtonState.SAVE -> {
                    // Save the user's input and navigate to a new screen
                    saveInputs()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            saveinputbutton.text = when (buttonState) {
                ButtonState.NEXT -> "Next"
                ButtonState.SAVE -> "Save"
            }
        }


    }


    private fun saveInputs() {
        val companyName = companynameinput.text.toString()
        val companyAddress = companyaddressinput.text.toString()
        val beneficiaryName = beneficiarynameinput.text.toString()
        val bankIfscCode = bankifsccodeinput.text.toString()
        val accountNumber = accountnumberinput.text.toString()
        val bankName = banknameinput.text.toString()
        val upiId = upiidinput.text.toString()
        val gstNumber = gstnumberinput.text.toString()
        val phoneNumber = phonenumberinput.text.toString()
        val emailId = emailinput.text.toString()

        val editor = sharedPreferences.edit()
        editor.putString("company_name", companyName)
        editor.putString("company_address", companyAddress)
        editor.putString("beneficiary_name", beneficiaryName)
        editor.putString("bank_ifsc_code", bankIfscCode)
        editor.putString("account_number", accountNumber)
        editor.putString("bank_name", bankName)
        editor.putString("upi_id", upiId)
        editor.putString("gst_number", gstNumber)
        editor.putString("phone_number", phoneNumber)
        editor.putString("email_id", emailId)
        editor.apply()
    }
}