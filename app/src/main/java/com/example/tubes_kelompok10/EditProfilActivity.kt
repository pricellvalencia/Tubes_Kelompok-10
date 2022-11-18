package com.example.tubes_kelompok10

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class EditProfilActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener(View.OnClickListener {
            val moveRegister = Intent(this@EditProfilActivity, HomeActivity::class.java)
            startActivity(moveRegister)
        })
    }
}