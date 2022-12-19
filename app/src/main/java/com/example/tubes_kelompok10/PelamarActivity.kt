package com.example.tubes_kelompok10

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.example.tubes_kelompok10.adapters.PelamarAdapter
import com.example.tubes_kelompok10.api.LowonganApi
import com.example.tubes_kelompok10.models.Pelamar
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PelamarActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    private var srPelamar: SwipeRefreshLayout? = null
    private var adapter: PelamarAdapter? = null
    private var svPelamar: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object {
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelamar)


        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srPelamar =  findViewById(R.id.sr_pelamar)
        svPelamar = findViewById(R.id.sv_pelamar)

        srPelamar?.setOnRefreshListener( { allPelamar() })
        svPelamar?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val  i = Intent( this@PelamarActivity, AddEditPelamarActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_pelamar)
        adapter = PelamarAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allPelamar()


    }

    private fun allPelamar() {
        srPelamar!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest (Method.GET, LowonganApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                var pelamar: Array<Pelamar> =
                    gson.fromJson(response, Array<Pelamar>::class.java)

                adapter!!.setPelamarList(pelamar)
                adapter!!.filter.filter(svPelamar!!.query)
                srPelamar!!.isRefreshing = false

                if (pelamar.isEmpty())
                    Toast.makeText(this@PelamarActivity, "Data kosong", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(this@PelamarActivity, "Data Berhasil Diambil!!", Toast.LENGTH_SHORT)
                        .show()
            }, Response.ErrorListener { error ->
                srPelamar!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@PelamarActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@PelamarActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return  headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun deletePelamar(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, LowonganApi.DELETE_URL + id, Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var pelamar = gson.fromJson(response, Pelamar::class.java)
                if(pelamar != null)
                    Toast.makeText(this@PelamarActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                allPelamar()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@PelamarActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: java.lang.Exception) {
                    Toast.makeText(this@PelamarActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "applicatipn/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allPelamar()
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.GONE
        }
    }
}