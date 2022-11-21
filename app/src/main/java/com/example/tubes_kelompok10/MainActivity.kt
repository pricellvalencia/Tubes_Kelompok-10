package com.example.tubes_kelompok10

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.util.ResourceBundle.getBundle

class MainActivity : AppCompatActivity() {
    //Atribut yang akan kita pakai
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout

    lateinit var  mBundle: Bundle


    lateinit var user: String
    lateinit var pass: String
    lateinit var email : String
    lateinit var phone: String
    lateinit var tgl: String


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent: Intent =intent

        // Mengambil data register ketika sudah register
        if (intent.hasExtra("register")) {
            getBundle()
            setText()
        }

        //ubah Title pada App Bar Aplikasi
        setTitle("")

        //hubungkan variabel dengan view di layoutnya
        inputUsername = findViewById(R.id.inputanLayoutUsername)
        inputPassword = findViewById(R.id.inputanLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)

        val btnRegister: Button = findViewById(R.id.btnRegister)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        btnRegister.setOnClickListener(View.OnClickListener {
            val moveRegister = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        })

        //aksi pada btnLogin
        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val usertemp: String = inputUsername.getEditText()?.getText().toString()
            val passtemp: String = inputPassword.getEditText()?.getText().toString()

            if(intent.hasExtra("register")){
                //pengecekan apakah inputan username kosong
                if (usertemp.isEmpty()) {
                    inputUsername.setError("Username must be filled with text")
                    checkLogin = false
                }
                //pengecekan apakah inputan password kosong
                if (passtemp.isEmpty()) {
                    inputPassword.setError("Password must be filled with text")
                    checkLogin = false
                }
                if(usertemp==user&&passtemp==pass) checkLogin=true
            }

            if (!checkLogin) return@OnClickListener
            val moveHome = Intent(this@MainActivity, LowonganActivity::class.java)
            moveHome.putExtra("Person", mBundle)
            startActivity(moveHome)

        })

    }

    fun setText() {
        inputUsername = findViewById(R.id.inputanLayoutUsername)
        inputUsername.getEditText()?.setText(user)
    }

    fun getBundle() {
        mBundle = intent.getBundleExtra("register")!!
        user = mBundle.getString("username")!!
        pass = mBundle.getString("password")!!
        email = mBundle.getString("email")!!
        phone = mBundle.getString("noPhone")!!
        tgl = mBundle.getString("tanggalLahir")!!

    }
}