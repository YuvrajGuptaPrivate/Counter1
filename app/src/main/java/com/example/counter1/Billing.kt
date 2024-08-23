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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import androidx.activity.result.contract.ActivityResultContracts
import java.util.Date
import java.util.Locale

class Billing : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InvoiceAdapter
    private lateinit var fName: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_billing, container, false)


        recyclerView = view.findViewById(R.id.InvoiceRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val newInvoiceButton = view.findViewById<Button>(R.id.NewInvoice)
        newInvoiceButton.setOnClickListener {
            val intent = Intent(requireContext(), InvoiceScreen::class.java)
            startActivity(intent)
        }

        val editInvoiceButton = view.findViewById<Button>(R.id.EditTemplate)
          editInvoiceButton.setOnClickListener {
            val intent = Intent(requireContext(), Inputs::class.java)
            startActivity(intent)
        }




        val invoices = listOf(
            Invoice(1, "John Doe", "2022-01-01", true),
            Invoice(2, "Jane Doe", "2022-01-02", false),
            Invoice(3, "Bob Smith", "2022-01-03", true),
        )

        adapter = InvoiceAdapter(invoices) { invoice ->
            Toast.makeText(context, "Clicked on invoice ${invoice.id}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!checkPermission()) {
            requestPermission()
        }

        view.findViewById<View>(R.id.createBtn).setOnClickListener {
            val fileName = getFileName()
            fName = fileName
            layoutTOimageConverter()
        }



    }

    private fun getFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateAndTime = sdf.format(Date())
        return "invoice_$currentDateAndTime"
    }

    private fun layoutTOimageConverter() {
        if (checkPermission()) {
            val layout = requireView().findViewById<LinearLayout>(R.id.sllayout)
            val file = saveBitMap(requireContext(), layout)
            if (file != null) {
                Log.i("TAG", "Drawing saved to the gallery!")
                Toast.makeText(requireContext(), "Processing", Toast.LENGTH_SHORT).show()
                try {
                    imageToPDF()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            } else {
                Log.i("TAG", "Oops! Image could not be saved.")
                Toast.makeText(requireContext(), "Click Again !", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "PDF Generated successfully!..", Toast.LENGTH_SHORT).show()
            savePDFToLocalStorage()
            val intent = Intent(requireContext(), Billing::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("Error", "Error generating PDF: $e")
            Toast.makeText(requireContext(), "Error generating PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePDFToLocalStorage() {
        val pdfFile = File(requireContext().filesDir, "invoice.pdf")
        val fos = FileOutputStream(pdfFile)
        val document = Document()
        PdfWriter.getInstance(document, fos)
        document.open()
        val img = Image.getInstance("${Environment.getExternalStorageDirectory()}/Pictures/Download/$fName.jpg")
        document.add(img)
        document.close()
        fos.close()
        Toast.makeText(requireContext(), "PDF saved to local storage", Toast.LENGTH_SHORT).show()
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
            val result = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
            val result1 = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data = Uri.parse("package:${requireContext().packageName}")

            val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.d("Permission", "Permission granted")
                    Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("Permission", "Permission denied")
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            startActivityForResult.launch(intent)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1100)
        }
    }
}

data class Invoice(val id: Int, val clientName: String, val date: String, val isPaid: Boolean)





































/* import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.provider.Settings
import android.util.Log
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import androidx.activity.result.contract.ActivityResultContracts

class Billing : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InvoiceAdapter
    private lateinit var fName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_billing, container, false)

        recyclerView = view.findViewById(R.id.InvoiceRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val invoices = listOf(
            Invoice(1, "John Doe", "2022-01-01", true),
            Invoice(2, "Jane Doe", "2022-01-02", false),
            Invoice(3, "Bob Smith", "2022-01-03", true),
        )

        adapter = InvoiceAdapter(invoices) { invoice ->
            Toast.makeText(context, "Clicked on invoice ${invoice.id}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!checkPermission()) {
            requestPermission()
        }

        // Add strict mode code on app initialize
        val builder = StrictMode.VmPolicy.Builder()
            .detectAll()
            .penaltyLog()
        StrictMode.setVmPolicy(builder.build())

        view.findViewById<View>(R.id.createBtn).setOnClickListener {
            layoutTOimageConverter()
        }
    }

    private fun layoutTOimageConverter() {
        if (checkPermission()) {
            val layout = requireView().findViewById<LinearLayout>(R.id.sllayout)
            val file = saveBitMap(requireContext(), layout)
            if (file != null) {
                Log.i("TAG", "Drawing saved to the gallery!")
                Toast.makeText(requireContext(), "Processing", Toast.LENGTH_SHORT).show()
                try {
                    imageToPDF()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            } else {
                Log.i("TAG", "Oops! Image could not be saved.")
                Toast.makeText(requireContext(), "Click Again !", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "PDF Generated successfully!..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Error", "Error generating PDF: $e")
            Toast.makeText(requireContext(), "Error generating PDF", Toast.LENGTH_SHORT).show()
        }
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
        } catch (e: IOException)
        {
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
        val result = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }
}

private fun requestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.data = Uri.parse("package:${requireContext().packageName}")

        val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("Permission", "Permission granted")
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("Permission", "Permission denied")
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        startActivityForResult.launch(intent)
    } else {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1100)
    }
}


}

data class Invoice(val id: Int, val clientName: String, val date: String, val isPaid: Boolean)

 */