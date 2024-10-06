package com.example.counter1.Invoicing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.icu.text.SimpleDateFormat
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
import androidx.lifecycle.Observer
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.counter1.Db.AppDATABASE
import com.example.counter1.Db.InventoryDao
import com.example.counter1.Db.InventoryDataClass
import com.example.counter1.Db.Order
import com.example.counter1.Db.OrderDao
import com.example.counter1.Db.Sales
import com.example.counter1.Db.SalesDao
import com.example.counter1.R
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

data class Item(val name: String, var quantity: String, var sellingprice: String, val description: String)

data class OrderedItem(
    val itemId: Int,
    val itemName: String,
    val quantity: Int,
    val unitprice: Int
)

class InvoiceScreen : AppCompatActivity() {

    private lateinit var fName: String
    private lateinit var layout: LinearLayout
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: InventoryDataForInvoiceSCreenadapter
    private lateinit var viewModel: InvoiceScreenViewModel
    private lateinit var inventoryDao : InventoryDao
    private lateinit var salesDao: SalesDao
    private lateinit var salesViewModel: SalesViewModel
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orderDao: OrderDao

    private lateinit var billrecyclerview : RecyclerView
    private lateinit var billadapter : BillItemAdapter
    private var isClicked : Boolean = false

    private lateinit var totalcartValuetextview : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_screen)
        layout = findViewById(R.id.templateutil)
        button1 = findViewById(R.id.printbtn)
        button2 = findViewById(R.id.proceedbtn)
        totalcartValuetextview = findViewById(R.id.totalcartvalue)

        recyclerView = findViewById(R.id.invoicescreenrecyclerview)

        salesDao = AppDATABASE.getinstance(applicationContext).salesDao()
        val salesFactory = SalesViewModelFactory(salesDao)
        salesViewModel = ViewModelProvider(this, salesFactory).get(SalesViewModel::class.java)

        inventoryDao = AppDATABASE.getinstance(applicationContext).inventoryDao()
        val factory = InvoiceScreenViewFactory(inventoryDao)
        viewModel = ViewModelProvider(this,factory).get(InvoiceScreenViewModel::class.java)

        orderDao = AppDATABASE.getinstance(applicationContext).orderDao()
        val orderfactory = OrderVMFactory(orderDao)
        orderViewModel = ViewModelProvider(this,orderfactory).get(OrderViewModel::class.java)

        recyclerViewAdapter = InventoryDataForInvoiceSCreenadapter(this,this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.AllInventory.observe(this, Observer { inventory: List<InventoryDataClass> ->
            recyclerViewAdapter.updateInventoryList(inventory)
            recyclerViewAdapter.notifyDataSetChanged()
        })

        billrecyclerview = findViewById(R.id.BillItemRecyclerview)
        billrecyclerview.layoutManager = LinearLayoutManager(this)
        billadapter = BillItemAdapter(this, emptyList())
        billrecyclerview.adapter = billadapter





        button2.setOnClickListener {

            isClicked=true
             updateTemplateLayout()
            recyclerViewAdapter.orderedItemsList.observe(this, Observer {
                OrderedItem ->
            recyclerViewAdapter.totalCartValue.observe(this, Observer {
                totalCartValue ->
                    proceedWithOrder(OrderedItem, totalCartValue)
                displayItemsInRecyclerView(OrderedItem)
                totalcartValuetextview.text = "Rs $totalCartValue"

                })
            })



        }

        //pdf creater
        button1.setOnClickListener {

            if (isClicked==true){
                val fileName = getFileName()
                fName = fileName
                layoutTOimageConverter()
                isClicked = false
            }
            else{
                Toast.makeText(this,"Please click on Proceed Button before printing",Toast.LENGTH_SHORT).show()
            }

        }


        if (!checkPermission()) {
            requestPermission()
        }
        updateTemplateLayoutCompany()

    }

     fun updateQuantity(itemName: String, newQuantity: Int) {
            viewModel.updateInventoryitem(itemName, newQuantity)
    }

    fun insertSalesData(sales: Sales){
        salesViewModel.insertSalesData(sales)
    }

    fun storeOrder( customerName: String, orderedItems: List<OrderedItem>, TotalCartValue: Long): Order {
        val gson = Gson()
        val itemsOrderedJson = gson.toJson(orderedItems) // Convert the list of ordered items to a JSON string
        return Order(
            0,
            customerName = customerName,
            itemsOrdered = itemsOrderedJson,  // Store JSON string in the database
            TotalCartValue
        )
    }

    fun proceedWithOrder(orderedItemsList: List<OrderedItem>, totalCartValue: Long) {

        val customerNameInput: TextInputEditText = findViewById(R.id.customername_input)
        val customerName = customerNameInput.text.toString()
        val data = storeOrder(customerName, orderedItemsList, totalCartValue)
        // Perform further actions like updating the UI or moving to the next screen.
        orderViewModel.insertorderData(data)
    }

    fun displayItemsInRecyclerView(items: List<OrderedItem>) {
        billadapter.updateItems(items)
        billrecyclerview.adapter = billadapter
    }











    override fun onResume() {
        super.onResume()
        recyclerViewAdapter = InventoryDataForInvoiceSCreenadapter(this,this )
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun updateTemplateLayout() {
        val customerNameInput: TextInputEditText = findViewById(R.id.customername_input)
        val customerAddressInput: TextInputEditText = findViewById(R.id.customerAddress_input)
        val customerEmailInput: TextInputEditText = findViewById(R.id.CustomerEmail_input)
        val customerGstInput: TextInputEditText = findViewById(R.id.CustomerGst_input)
        val customerPhoneInput : TextInputEditText = findViewById(R.id.customerphone_number_input)

        val customerNameOutput: TextView = findViewById(R.id.customername_output)
        val customerAddressOutput: TextView = findViewById(R.id.customerAddress_output)
        val customerEmailOutput: TextView = findViewById(R.id.CustomerEmail_output)
        val customerGstOutput: TextView = findViewById(R.id.CustomerGst_output)
        val invoiceNumberOutput: TextView = findViewById(R.id.invoice_number_output)
        val dateOutput: TextView = findViewById(R.id.date_output)


        // Check if customer name or phone number are empty
        val customerName = customerNameInput.text?.toString().orEmpty()
        val customerPhone = customerPhoneInput.text?.toString().orEmpty()

        if (customerName.isBlank() || customerPhone.isBlank()) {
            Toast.makeText(this, "Customer Name and Phone Number are mandatory", Toast.LENGTH_SHORT).show()
            return // Exit the function if mandatory fields are missing
        }

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






    ///






    // Modified updateTemplateLayout function
    private fun updateTemplateLayoutCompany() {
        val sharedPreferences = getSharedPreferences("inputs_data", MODE_PRIVATE)

        val companyNameOutput: TextView = findViewById(R.id.company_name_output)
        val companyAddressOutput: TextView = findViewById(R.id.company_address_output)
        val gstNumberOutput: TextView = findViewById(R.id.gst_number_output)
        val emailIdOutput: TextView = findViewById(R.id.email_id_output)
        val phoneNumberOutput: TextView = findViewById(R.id.phone_number_output)
        val footercompanyOutput: TextView = findViewById(R.id.footercompanyname)


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
        footercompanyOutput.text= companyName
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
            val dirpath = getExternalFilesDir(null).toString()
            val clientName = findViewById<TextView>(R.id.customername_output).text.toString().replace("PARTY'S NAME - ", "")
            val invoiceNumber = findViewById<TextView>(R.id.invoice_number_output).text.toString().replace("INVOICE NUMBER: ", "")
            val date = findViewById<TextView>(R.id.date_output).text.toString().replace("DATE: ", "")
            val fileName = "$clientName-$invoiceNumber-$date.jpg"
            val imageFile = "$dirpath/$fileName"
            val pdfFileName = "$clientName-$invoiceNumber-$date.pdf"
            val pdfFile = File("$dirpath/$pdfFileName")

            // Check if the image file exists
            if (!File(imageFile).exists()) {
                Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show()
                return
            }

            // Create the PDF file
            pdfFile.parentFile?.mkdirs()
            PdfWriter.getInstance(document, FileOutputStream(pdfFile))
            document.open()
            val img = Image.getInstance(imageFile)
            val scaler = (document.pageSize.width - document.leftMargin() - document.rightMargin() - 0) / img.width * 100
            img.scalePercent(scaler)
            img.alignment = Image.ALIGN_CENTER or Image.ALIGN_TOP
            document.add(img)
            document.close()
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show()
            savePDFToLocalStorage()

            // Commit the fragment transaction on the main thread
            runOnUiThread {
               finish()
            }
        } catch (e: Exception) {
            Log.e("Error", "Error generating PDF: $e")
            Toast.makeText(this, "Error generating PDF", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveBitMap(context: Context, drawView: View): File? {
        val dirpath = getExternalFilesDir(null).toString()
        val clientName = findViewById<TextView>(R.id.customername_output).text.toString().replace("PARTY'S NAME - ", "")
        val invoiceNumber = findViewById<TextView>(R.id.invoice_number_output).text.toString().replace("INVOICE NUMBER: ", "")
        val date = findViewById<TextView>(R.id.date_output).text.toString().replace("DATE: ", "")
        val fileName = "$clientName-$invoiceNumber-$date.jpg"
        val pictureFile = File("$dirpath/$fileName")
        val bitmap = getBitmapFromView(drawView)
        try {
            pictureFile.createNewFile()
            val oStream = FileOutputStream(pictureFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream)
            oStream.flush()
            oStream.close()
            scanGallery(context, pictureFile.absolutePath) // Scan the image file after saving it
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("TAG", "There was an issue saving the image.")
        }
        return pictureFile
    }

    private fun savePDFToLocalStorage() {
        val dirpath = getExternalFilesDir(null).toString()
        val clientName = findViewById<TextView>(R.id.customername_output).text.toString().replace("PARTY'S NAME - ", "")
        val invoiceNumber = findViewById<TextView>(R.id.invoice_number_output).text.toString().replace("INVOICE NUMBER: ", "")
        val date = findViewById<TextView>(R.id.date_output).text.toString().replace("DATE: ", "")
        val pdfFileName = "$clientName-$invoiceNumber-$date.pdf"
        val sourceFile = File("$dirpath/$pdfFileName")
        val destinationFile = File(this.filesDir, pdfFileName)
        sourceFile.copyTo(destinationFile)
        Toast.makeText(this, "PDF saved to local storage", Toast.LENGTH_SHORT).show()
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

    private fun scanGallery(context: Context, filePath: String) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val file = File(filePath)
        val contentUri = Uri.fromFile(file)
        mediaScanIntent.setData(contentUri)
        context.sendBroadcast(mediaScanIntent)
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

