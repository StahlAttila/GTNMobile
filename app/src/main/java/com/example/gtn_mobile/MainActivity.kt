package com.example.gtn_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        isLoggedIn()

        binding.buttonLogin.setOnClickListener {
            var username = binding.inputUsername.text.toString()
            var password = binding.inputPw.text.toString()
            login(username, password)
        }

        binding.tvSignUp.setOnClickListener {
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
                if (loginResponse.jwt.isNotBlank()) {
                    saveToPrefs("JWT_TOKEN", "Bearer ${loginResponse.jwt}")
                    saveToPrefs("USERNAME", loginResponse.username)
                    gotToGameActivity(loginResponse.username)
                } else {
                    Log.d("My-Test", "Success: ${loginResponse.message}")
                }
            }
        })
    }

    fun gotToGameActivity(username: String) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun readSharedPref(key: String): String {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val value = sharedPreferences.getString(key, null)

        return value.toString()
    }

    private fun saveToPrefs(key :String, value :String) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString(key, value)
        }.apply()
    }

    private fun isLoggedIn() {
        val token = readSharedPref("JWT_TOKEN")
        if(token != "null") {
            val username = readSharedPref("USERNAME")
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

}