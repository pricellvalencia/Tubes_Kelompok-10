package com.example.tubes_kelompok10

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes_kelompok10.api.LowonganApi
import com.example.tubes_kelompok10.models.Pelamar
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class AddEditPelamarActivity : AppCompatActivity() {

    private var etNama: EditText? = null
    private var etJenisKelamin: EditText? = null
    private var etTglLahir: EditText? = null
    private var etAlamat: EditText? = null
    private var etEmail: EditText? = null
    private var etPendidikan: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_pelamar)

        queue = Volley.newRequestQueue(this)
        etNama = findViewById(R.id.et_nama)
        etJenisKelamin = findViewById(R.id.et_jenisKelamin)
        etTglLahir = findViewById(R.id.et_tglLahir)
        etAlamat = findViewById(R.id.et_alamatPelamar)
        etEmail = findViewById(R.id.et_email)
        etPendidikan = findViewById(R.id.et_pendidikan)
        layoutLoading = findViewById(R.id.layout_loading)


        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener {finish()}
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if(id==-1L) {
            tvTitle.setText("Tambah Pelamar")
            btnSave.setOnClickListener { createPelamar() }
        } else {
            tvTitle.setText("Edit Pelamar")
            getPelamarById(id)

            btnSave.setOnClickListener {updatePelamar(id)}
        }

    }


    private fun getPelamarById(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, LowonganApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val pelamar = gson.fromJson(response, Pelamar::class.java)

                etNama!!.setText(pelamar.nama)
                etJenisKelamin!!.setText(pelamar.jeniskelamin)
                etTglLahir!!.setText(pelamar.tglLahir)
                etAlamat!!.setText(pelamar.alamat)
                etEmail!!.setText(pelamar.email)
                etPendidikan!!.setText(pelamar.pendidikan)

                Toast.makeText(this@AddEditPelamarActivity, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditPelamarActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditPelamarActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createPelamar(){
        setLoading(true)

        if (etNama!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditPelamarActivity, "Nama must be filled", Toast.LENGTH_SHORT).show()
        }
        if (etJenisKelamin!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditPelamarActivity, "Jenis kelamin must be filled", Toast.LENGTH_SHORT).show()
        }
        if (etTglLahir!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditPelamarActivity, "Tanggal lahir must be filled", Toast.LENGTH_SHORT).show()
        }
        if (etAlamat!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditPelamarActivity, "Alamat must be filled", Toast.LENGTH_SHORT).show()
        }
        else if (etEmail!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditPelamarActivity, "Email must be filled", Toast.LENGTH_SHORT).show()
        }
        else if (etPendidikan!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditPelamarActivity, "Pendidikan must be filled", Toast.LENGTH_SHORT)
                .show()
        }
        else{
            val pelamar = Pelamar(
                etNama!!.text.toString(),
                etJenisKelamin!!.text.toString(),
                etTglLahir!!.text.toString(),
                etAlamat!!.text.toString(),
                etEmail!!.text.toString(),
                etPendidikan!!.text.toString()

            )

            val stringRequest: StringRequest =
                object : StringRequest(Method.POST, LowonganApi.ADD_URL, Response.Listener { response ->
                    val gson = Gson()
                    var pelamar = gson.fromJson(response, Pelamar::class.java)

                    if(pelamar != null)
                        Toast.makeText(this@AddEditPelamarActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

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
                            this@AddEditPelamarActivity,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception){
                        Toast.makeText(this@AddEditPelamarActivity, e.message,Toast.LENGTH_SHORT).show()
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
                        val requestBody = gson.toJson(pelamar)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
            queue!!.add(stringRequest)
        }
    }

    private fun updatePelamar(id: Long) {
        setLoading(true)

        val pelamar = Pelamar(
            etNama!!.text.toString(),
            etJenisKelamin!!.text.toString(),
            etTglLahir!!.text.toString(),
            etAlamat!!.text.toString(),
            etEmail!!.text.toString(),
            etPendidikan!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, LowonganApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()

                var pelamar = gson.fromJson(response, Pelamar::class.java)

                if(pelamar != null)
                    Toast.makeText(this@AddEditPelamarActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

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
                        this@AddEditPelamarActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditPelamarActivity, e.message, Toast.LENGTH_SHORT).show()
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
                val requestBody = gson.toJson(pelamar)
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