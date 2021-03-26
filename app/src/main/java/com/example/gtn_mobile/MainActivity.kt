package com.example.gtn_mobile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gtn_mobile.databinding.ActivityMainBinding
import com.example.gtn_mobile.httprequests.HttpRequestCallBuilder
import com.example.gtn_mobile.models.LoginResponse
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val callBuilder = HttpRequestCallBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonLogin.setOnClickListener {
            Log.d("My-Test", "login button clicked")
            var username = binding.inputUsername.text.toString()
            var password = binding.inputPw.text.toString()
            Log.d("My-Test", "before login: $username $password")
            login(username, password)
        }

        binding.tvSignUp.setOnClickListener {
            Log.d("My-Test", "singin link clicked")
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    fun login(username: String, password: String) {

        val url = "https://gtn-api.herokuapp.com/sign-in"
        val params = HashMap<String, String>()
        params.put("username", username)
        params.put("password", password)

        callBuilder.buildPostCall(url, params).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("My-Test", "Failed to execute request!")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val loginResponse = gson.fromJson(body, LoginResponse::class.java)
                if (!loginResponse.jwt.isNullOrBlank()) {
                    gotToGameActivity()
                    Log.d("My-Test", "Success: $body")
                } else {
                    Log.d("My-Test", "Success: ${loginResponse.message}")
                    Handler(Looper.getMainLooper()).post(Runnable {
                        Toast.makeText(
                            applicationContext,
                            loginResponse.message, Toast.LENGTH_LONG
                        ).show()
                    })
                }
            }
        })
    }

    fun gotToGameActivity() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }


}