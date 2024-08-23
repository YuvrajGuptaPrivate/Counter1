package com.example.counter1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Inputs : AppCompatActivity() {

    private lateinit var companyNameEditText: EditText
    private lateinit var companyAddressEditText: EditText
    private lateinit var beneficiaryNameEditText: EditText
    private lateinit var bankIfscCodeEditText: EditText
    private lateinit var accountNumberEditText: EditText
    private lateinit var bankNameEditText: EditText
    private lateinit var upiIdEditText: EditText
    private lateinit var gstNumberEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var contactInformationEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inputs)

        companyNameEditText = findViewById(R.id.company_name_edit_text)
        companyAddressEditText = findViewById(R.id.company_address_edit_text)
        beneficiaryNameEditText = findViewById(R.id.beneficiary_name_edit_text)
        bankIfscCodeEditText = findViewById(R.id.bank_ifsc_code_edit_text)
        accountNumberEditText = findViewById(R.id.account_number_edit_text)
        bankNameEditText = findViewById(R.id.bank_name_edit_text)
        upiIdEditText = findViewById(R.id.upi_id_edit_text)
        gstNumberEditText = findViewById(R.id.gst_number_edit_text)
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text)
        contactInformationEditText = findViewById(R.id.contact_information_edit_text)
        saveButton = findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            val companyName = companyNameEditText.text.toString().trim()
            val companyAddress = companyAddressEditText.text.toString().trim()
            val beneficiaryName = beneficiaryNameEditText.text.toString().trim()
            val bankIfscCode = bankIfscCodeEditText.text.toString().trim()
            val accountNumber = accountNumberEditText.text.toString().trim()
            val bankName = bankNameEditText.text.toString().trim()
            val upiId = upiIdEditText.text.toString().trim()
            val gstNumber = gstNumberEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            val contactInformation = contactInformationEditText.text.toString().trim()

            if (companyName.isEmpty() || companyAddress.isEmpty() || beneficiaryName.isEmpty() || bankIfscCode.isEmpty() || accountNumber.isEmpty() || bankName.isEmpty() || upiId.isEmpty() || gstNumber.isEmpty() || phoneNumber.isEmpty() || contactInformation.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences = this.getSharedPreferences("user_data", MODE_PRIVATE)
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
            editor.putString("contact_information", contactInformation)
            editor.apply()

            try {
                editor.apply()
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error saving data: $e", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}