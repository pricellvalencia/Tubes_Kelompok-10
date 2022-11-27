package com.example.tubes_kelompok10

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.tubes_kelompok10.databinding.ActivityCvactivityBinding
import com.example.tubes_kelompok10.databinding.ActivityMainBinding
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CVActivity : AppCompatActivity() {

    private var binding: ActivityCvactivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCvactivityBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)

        binding!!.buttonSave.setOnClickListener {
            val nama = binding!!.editTextName.text.toString()
            val ttl = binding!!.editTextTTL.text.toString()
            val tlp = binding!!.editTextTelepon.text.toString()
            val email = binding!!.editTextEmail.text.toString()
            val pendidikan = binding!!.editTextPendidikan.text.toString()
            val pengalamankerja = binding!!.editTextPengalamanKerja.text.toString()


            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (nama.isEmpty() && ttl.isEmpty() && tlp.isEmpty() && email.isEmpty() && pendidikan.isEmpty() && pengalamankerja.isEmpty()) {
                        Toast.makeText(applicationContext,"Semuanya Tidak boleh Kosong" , Toast.LENGTH_SHORT).show()
                    }else {
                        createPdf(nama, ttl, tlp, email, pendidikan, pengalamankerja)
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )
    private fun createPdf(nama: String, ttl: String, tlp: String, email: String, pendidikan: String, pengalamankerja: String) {
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "pdf_tubes_kelompok10.pdf")
        FileOutputStream(file)

        //inisaliasi pembuatan PDF
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f, 5f, 5f, 5f)

        val cvpengguna = Paragraph("CURRICULUM VITAE").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                        Berikut adalah
                        CV Pelamar
                        """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        //proses pembuatan table
        val width = floatArrayOf(100f, 100f)
        val table = Table(width)
        //pengisian table dengan data-data
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Diri")))
        table.addCell(Cell().add(Paragraph(nama)))
        table.addCell(Cell().add(Paragraph("Tempat Tanggal Lahir")))
        table.addCell(Cell().add(Paragraph(ttl)))
        table.addCell(Cell().add(Paragraph("No Telepon")))
        table.addCell(Cell().add(Paragraph(tlp)))
        table.addCell(Cell().add(Paragraph("Email")))
        table.addCell(Cell().add(Paragraph(email)))
        table.addCell(Cell().add(Paragraph("Pendidikan")))
        table.addCell(Cell().add(Paragraph(pendidikan)))
        table.addCell(Cell().add(Paragraph("Pengamalan kerja")))
        table.addCell(Cell().add(Paragraph(pengalamankerja)))

        //pembuatan QR CODE secara generate dengan bantuan IText7
        val barcodeQRCode = BarcodeQRCode(
            """
                                        $nama
                                        $ttl
                                        $tlp
                                        $email
                                        $pendidikan
                                        $pengalamankerja
                                        """.trimIndent())

        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)

        document.add(cvpengguna)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_LONG).show()
    }
}