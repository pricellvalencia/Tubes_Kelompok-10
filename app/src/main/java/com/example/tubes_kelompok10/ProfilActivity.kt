package com.example.tubes_kelompok10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.view.View
import android.widget.Button

class ProfilActivity : AppCompatActivity() {
    lateinit var mBundle: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        // mBundle = intent.getBundleExtra("Personn")!!
        //  setData()
        val btnEditDiProfil: Button = findViewById(R.id.btnEditDiProfil)
        btnEditDiProfil.setOnClickListener(View.OnClickListener {
            val moveEditProfil = Intent(this@ProfilActivity, EditProfilActivity::class.java)
            startActivity(moveEditProfil)
        })

        val btnEditFoto: Button = findViewById(R.id.btnEditFoto)
        btnEditFoto.setOnClickListener(View.OnClickListener {
            val moveEditFoto = Intent(this@ProfilActivity, EditFotoActivity::class.java)
            startActivity(moveEditFoto)
        })

        val btnCv: Button = findViewById(R.id.btnCV)
        btnCv.setOnClickListener(View.OnClickListener {
            val moveCV = Intent(this@ProfilActivity, CVActivity::class.java)
            startActivity(moveCV)
        })
    }

    fun setData() {
        val username: TextView = findViewById(R.id.username)
        val email: TextView = findViewById(R.id.email)
        val tanggalLahir: TextView = findViewById(R.id.tanggallahir)
        val nomorTelepon: TextView = findViewById(R.id.phone)

        username.text = mBundle.getString("username")
        email.text = mBundle.getString("email")
        tanggalLahir.text = mBundle.getString("tanggalLahir")
        nomorTelepon.text = mBundle.getString("noTelepon")
    }


}