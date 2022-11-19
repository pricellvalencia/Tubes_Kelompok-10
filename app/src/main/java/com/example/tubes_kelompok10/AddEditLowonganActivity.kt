package com.example.tubes_kelompok10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes_kelompok10.api.LowonganApi
import com.example.tubes_kelompok10.models.Lowongan
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditLowonganActivity : AppCompatActivity() {

    private var etNamaPerusahaan: EditText? = null
    private var etPosisi: EditText? = null
    private var edTanggalPenutupan: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_lowongan)

        queue = Volley.newRequestQueue(this)
        etNamaPerusahaan = findViewById(R.id.et_namaPerusahaan)
        etPosisi = findViewById(R.id.et_posisi)
        edTanggalPenutupan = findViewById(R.id.ed_tanggalPenutupan)
        layoutLoading = findViewById(R.id.layout_loading)


        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener {finish()}
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if(id==-1L) {
            tvTitle.setText("Tambah Lowongan")
            btnSave.setOnClickListener { createLowongan() }
        } else {
            tvTitle.setText("Edit Lowongan")
            getLowonganById(id)

            btnSave.setOnClickListener {updateLowongan(id)}
        }

    }


    private fun getLowonganById(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, LowonganApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val lowongan = gson.fromJson(response, Lowongan::class.java)

                etNamaPerusahaan!!.setText(lowongan.namaperusahaan)
                etPosisi!!.setText(lowongan.posisi)
                edTanggalPenutupan!!.setText(lowongan.tanggalpenutupan)

                Toast.makeText(this@AddEditLowonganActivity, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditLowonganActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditLowonganActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun createLowongan(){
        setLoading(true)

        val lowongan = Lowongan(
            etNamaPerusahaan!!.text.toString(),
            etPosisi!!.text.toString(),
            edTanggalPenutupan!!.text.toString()

        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, LowonganApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var lowongan = gson.fromJson(response, Lowongan::class.java)

                if(lowongan != null)
                    Toast.makeText(this@AddEditLowonganActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditLowonganActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@AddEditLowonganActivity, e.message,Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(lowongan)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }

    private fun updateLowongan(id: Long) {
        setLoading(true)

        val lowongan = Lowongan(
            etNamaPerusahaan!!.text.toString(),
            etPosisi!!.text.toString(),
            edTanggalPenutupan!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, LowonganApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()

                var lowongan = gson.fromJson(response, Lowongan::class.java)

                if(lowongan != null)
                    Toast.makeText(this@AddEditLowonganActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditLowonganActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditLowonganActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(lowongan)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setLoading(isLoading: Boolean) {
        if(isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}