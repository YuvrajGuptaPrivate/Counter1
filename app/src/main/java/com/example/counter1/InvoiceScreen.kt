package com.example.counter1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import java.util.Locale

class InvoiceScreen : AppCompatActivity() {

    private lateinit var fName: String
    private lateinit var layout: LinearLayout
    private lateinit var button1: Button
    private lateinit var button2: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_screen)
        layout = findViewById(R.id.templateutil)
        button1 = findViewById(R.id.printbtn)
        button2 = findViewById(R.id.proceedbtn)


        button2.setOnClickListener {
                updateTemplateLayout()
            }

        button1.setOnClickListener {
                val fileName = getFileName()
                fName = fileName
                layoutTOimageConverter()
            }


        if (!checkPermission()) {
            requestPermission()
        }
        updateTemplateLayoutCompany()

    }

    private fun updateTemplateLayout() {
        val customerNameInput: TextInputEditText = findViewById(R.id.customername_input)
        val customerAddressInput: TextInputEditText = findViewById(R.id.customerAddress_input)
        val customerEmailInput: TextInputEditText = findViewById(R.id.CustomerEmail_input)
        val customerGstInput: TextInputEditText = findViewById(R.id.CustomerGst_input)

        val customerNameOutput: TextView = findViewById(R.id.customername_output)
        val customerAddressOutput: TextView = findViewById(R.id.customerAddress_output)
        val customerEmailOutput: TextView = findViewById(R.id.CustomerEmail_output)
        val customerGstOutput: TextView = findViewById(R.id.CustomerGst_output)
        val invoiceNumberOutput: TextView = findViewById(R.id.invoice_number_output)
        val dateOutput: TextView = findViewById(R.id.date_output)


        customerNameOutput.text = "PARTY'S NAME - ${customerNameInput.text}"
        customerAddressOutput.text = "ADDRESS: ${customerAddressInput.text}"
        customerEmailOutput.text = "Email ID: ${customerEmailInput.text}"
        customerGstOutput.text = "GSTIN: ${customerGstInput.text}"

        // Generate current date
        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        dateOutput.text = "DATE: $currentDate"

        // Generate unique invoice number
        val invoiceNumber = "INV-" + System.currentTimeMillis()
        invoiceNumberOutput.text = "INVOICE NUMBER: $invoiceNumber"
    }

    // Modified updateTemplateLayout function
     private fun updateTemplateLayoutCompany() {
        val sharedPreferences = getSharedPreferences("inputs_data", MODE_PRIVATE)

        val companyNameOutput: TextView = findViewById(R.id.company_name_output)
        val companyAddressOutput: TextView = findViewById(R.id.company_address_output)
        val gstNumberOutput: TextView = findViewById(R.id.gst_number_output)
        val emailIdOutput: TextView = findViewById(R.id.email_id_output)
        val phoneNumberOutput: TextView = findViewById(R.id.phone_number_output)

        val companyName = sharedPreferences.getString("company_name", "")
        val companyAddress = sharedPreferences.getString("company_address", "")
        val gstNumber = sharedPreferences.getString("gst_number", "")
        val emailId = sharedPreferences.getString("email_id", "")
        val phoneNumber = sharedPreferences.getString("phone_number", "")

        companyNameOutput.text = companyName
        companyAddressOutput.text = companyAddress
        gstNumberOutput.text = "GSTIN: $gstNumber"
        emailIdOutput.text = "Email ID: $emailId"
        phoneNumberOutput.text = "Phone Number: $phoneNumber"
    }

    private fun getFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateAndTime = sdf.format(Date())
        return "invoice_$currentDateAndTime"
    }

    private fun layoutTOimageConverter() {
        if (checkPermission()) {
            val file = saveBitMap(this, layout)
            if (file != null) {
                Log.i("TAG", "Drawing saved to the gallery!")
                Toast.makeText(this, "Processing", Toast.LENGTH_SHORT).show()
                try {
                    imageToPDF()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            } else {
                Log.i("TAG", "Oops! Image could not be saved.")
                Toast.makeText(this, "Click Again !", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }
    }

    @Throws(FileNotFoundException::class)
    private fun imageToPDF() {
        try {
            val document = Document()
            val dirpath = Environment.getExternalStorageDirectory().toString()
            PdfWriter.getInstance(document, FileOutputStream("$dirpath/$fName.pdf"))
            document.open()
            val img = Image.getInstance("$dirpath/Pictures/Download/$fName.jpg")
            val scaler = (document.pageSize.width - document.leftMargin() - document.rightMargin() - 0) / img.width * 100
            img.scalePercent(scaler)
            img.alignment = Image.ALIGN_CENTER or Image.ALIGN_TOP
            document.add(img)
            document.close()
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show()
            savePDFToLocalStorage()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val myFragment = Billing()
            fragmentTransaction.replace(R.id.Framelayout, myFragment)
            fragmentTransaction.commit()

        } catch (e: Exception) {
            Log.e("Error", "Error generating PDF: $e")
            Toast.makeText(this, "Error generating PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePDFToLocalStorage() {
        val pdfFile = File(this.filesDir, "invoice.pdf")
        val fos = FileOutputStream(pdfFile)
        val document = Document()
        PdfWriter.getInstance(document, fos)
        document.open()
        val img = Image.getInstance("${Environment.getExternalStorageDirectory()}/Pictures/Download/$fName.jpg")
        document.add(img)
        document.close()
        fos.close()
        Toast.makeText(this, "PDF saved to local storage", Toast.LENGTH_SHORT).show()
    }


    private fun saveBitMap(context: Context, drawView: View): File? {
        val pictureFileDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Download")
        if (!pictureFileDir.exists()) {
            val isDirectoryCreated = pictureFileDir.mkdirs()
            if (!isDirectoryCreated) {
                Log.i("ATG", "Can't create directory to save the image")
                return null
            }
        }
        val filename = "$pictureFileDir/$fName.jpg"
        val pictureFile = File(filename)
        val bitmap = getBitmapFromView(drawView)
        try {
            pictureFile.createNewFile()
            val oStream = FileOutputStream(pictureFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream)
            oStream.flush()
            oStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("TAG", "There was an issue saving the image.")
        }
        scanGallery(context, pictureFile.absolutePath)
        return pictureFile
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    private fun scanGallery(cntx: Context, path: String) {
        try {
            MediaScannerConnection.scanFile(cntx, arrayOf(path), null, object : MediaScannerConnection.OnScanCompletedListener {
                override fun onScanCompleted(path: String, uri: Uri) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            val result1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data = Uri.parse("package:${this.packageName}")

            val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.d("Permission", "Permission granted")
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("Permission", "Permission denied")
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            startActivityForResult.launch(intent)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1100)
        }
    }
}

