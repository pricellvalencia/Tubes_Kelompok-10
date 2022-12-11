package com.example.tubes_kelompok10

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
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
import com.example.tubes_kelompok10.adapters.LowonganAdapter
import com.example.tubes_kelompok10.api.LowonganApi
import com.example.tubes_kelompok10.models.Lowongan
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class LowonganActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    private var srLowongan: SwipeRefreshLayout? = null
    private var adapter: LowonganAdapter? = null
    private var svLowongan: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object {
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lowongan)

        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    val intent = Intent(this, LowonganActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_location -> {
                    val intent = Intent(this, LocationActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_profil -> {
                    var moveProfile: Intent
                    moveProfile = Intent(this, ProfilActivity::class.java)
                    moveProfile.putExtra("Person", intent.getBundleExtra("Person"))
                    startActivity(moveProfile)
                    return@setOnNavigationItemReselectedListener
                }
                R.id.menu_report ->{
                    val intent = Intent(this, ReportActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_exit -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

            }
        }
        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srLowongan =  findViewById(R.id.sr_lowongan)
        svLowongan = findViewById(R.id.sv_lowongan)

        srLowongan?.setOnRefreshListener( { allLowongan() })
        svLowongan?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
            val  i = Intent( this@LowonganActivity, AddEditLowonganActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_lowongan)
        adapter = LowonganAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allLowongan()

    }

    private fun allLowongan() {
        srLowongan!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest (Method.GET, LowonganApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                var lowongan: Array<Lowongan> =
                    gson.fromJson(response, Array<Lowongan>::class.java)

                adapter!!.setLowonganList(lowongan)
                adapter!!.filter.filter(svLowongan!!.query)
                srLowongan!!.isRefreshing = false

                if (lowongan.isEmpty())
                    Toast.makeText(this@LowonganActivity, "Data Berhasil Diambil!!", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(this@LowonganActivity, "Data Kosong", Toast.LENGTH_SHORT)
                        .show()
            }, Response.ErrorListener { error ->
                srLowongan!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@LowonganActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@LowonganActivity, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteLowongan(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, LowonganApi.DELETE_URL + id, Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var lowongan = gson.fromJson(response, Lowongan::class.java)
                if(lowongan != null)
                    Toast.makeText(this@LowonganActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                allLowongan()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@LowonganActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: java.lang.Exception) {
                    Toast.makeText(this@LowonganActivity, e.message, Toast.LENGTH_SHORT).show()
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
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allLowongan()
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