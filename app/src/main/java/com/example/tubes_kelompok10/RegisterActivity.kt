package com.example.tubes_kelompok10

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import android.view.View
import com.example.tubes_kelompok10.room.User
import com.example.tubes_kelompok10.room.UserDB
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tubes_kelompok10.api.LowonganApi
import com.example.tubes_kelompok10.databinding.ActivityRegisterBinding
import com.example.tubes_kelompok10.models.Lowongan
import com.example.tubes_kelompok10.models.Register
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId2 = 102
    val db by lazy { UserDB(this) }
    private lateinit var username : TextInputEditText
    private lateinit var email : TextInputEditText
    private lateinit var noPhone : TextInputEditText
    private lateinit var tglLahir : TextInputEditText
    private lateinit var password : TextInputEditText
    private lateinit var btnRegister : Button
    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("")

        queue = Volley.newRequestQueue( this)

        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        noPhone = findViewById(R.id.phone)
        tglLahir = findViewById(R.id.tanggallahir)
        password = findViewById(R.id.password)
        btnRegister = findViewById(R.id.RegisterBtn)


        RegisterBtn.setOnClickListener{
            var check = false
//            val intent = Intent(this, MainActivity::class.java)
//            val mBundle = Bundle()


            val username: String = username.text.toString()
            val password: String = password.text.toString()
            val email: String = email.text.toString()
            val tanggalLahir: String = tglLahir.text.toString()
            val noTelepon: String = noPhone.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Username must be filled", Toast.LENGTH_SHORT).show()
            }
            else if (email.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Email must be filled", Toast.LENGTH_SHORT).show()
            }
            else if (noTelepon.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Nomor Telepon must be filled", Toast.LENGTH_SHORT).show()
            }
            else if (tanggalLahir.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Tanggal Lahir must be filled", Toast.LENGTH_SHORT).show()
            }
            else if (password.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Password must be filled", Toast.LENGTH_SHORT).show()
            }
            else{
                check=true
                register()
//                mBundle.putString("username",username)
//                mBundle.putString("email",email)
//                mBundle.putString("noPhone",noTelepon)
//                mBundle.putString("tanggalLahir",tanggalLahir)
//                mBundle.putString("password",password)
//
//                intent.putExtra("register", mBundle)

                createNotificationChannel()

                /*if (username.text.toString().length == 0) {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.userDao().addUser(
                            User(
                                1, "a", "a", 1, "a", "a"
                            )
                        )
                        finish()
                    }

                    startActivity(intent)
                }

                CoroutineScope(Dispatchers.IO).launch {
                    db.userDao().addUser(
                        User(
                            (Math.random() * (10000 - 3 + 1)).toInt(),
                            username.text.toString(),
                            email.text.toString(),
                            (Math.random() * (10000 - 3 + 1)).toInt(),
                            tanggallahir.text.toString(),
                            password.text.toString()

                        )
                    )
                    finish()
                }

                 */

                sendNotification2()

//                startActivity(intent)
            }
        }

        val backlogin: Button = findViewById(R.id.backlogin)
        backlogin.setOnClickListener(View.OnClickListener {
            val moveRegister = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(moveRegister)
        })
    }

    private fun register() {
        val register = Register(
            binding.username.text.toString(),
            binding.email.text.toString(),
            binding.phone.text.toString(),
            binding.tanggallahir.text.toString(),
            binding.password.text.toString(),
        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, LowonganApi.register1, Response.Listener { response ->
//                val gson = Gson()
//                val JsonObject = JSONObJsonObject.getJSONArray("message").toString()ject(response)
//                var register = gson.fromJson(JsonObject.getJSONArray("message").toString(), Register::class.java)

                if (register != null)
                    Toast.makeText(
                        this@RegisterActivity,
                        JSONObject(response).getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()

                val intent = Intent(this, MainActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("username", binding.username.text.toString())
                mBundle.putString("password", binding.password.text.toString(),)

                intent.putExtra("register", mBundle)
                startActivity(intent)
                finish()


            }, Response.ErrorListener { error ->

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@RegisterActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
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
                    val requestBody = gson.toJson(register)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val channel2 = NotificationChannel(CHANNEL_ID_2, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }

    private fun sendNotification2() {
        val YesIntent = Intent(this, MainActivity::class.java)
        YesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val YesPendingIntent = PendingIntent.getActivity(this, 0, YesIntent, PendingIntent.FLAG_MUTABLE)
        val picture = BitmapFactory.decodeResource(resources, R.drawable.logospashscreen)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_supervised_user_circle_24)
            .setContentTitle(binding?.username?.text.toString())
            .setContentText(binding?.password?.text.toString())
            .setContentTitle("Register Berhasil!")
            .setContentText(binding?.username?.text.toString())
            .setLargeIcon(picture)
            .setColor(Color.BLUE)
            .addAction(R.drawable.ic_baseline_supervised_user_circle_24, "Yes", YesPendingIntent)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigLargeIcon(null)
                .bigPicture((picture)))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId2, builder.build())
        }
    }
}