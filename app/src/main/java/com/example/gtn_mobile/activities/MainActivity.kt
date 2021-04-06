package com.example.gtn_mobile.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gtn_mobile.databinding.ActivityMainBinding
import com.example.gtn_mobile.httprequests.HttpRequestCallBuilder
import com.example.gtn_mobile.models.RanksContainer
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val callBuilder = HttpRequestCallBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val username = intent.getStringExtra("username")
        if (username != null) {
            setWelcomeMessage(username)
            getRanks(username)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        isPlayerLoggedIn()
        val username = intent.getStringExtra("username")
        if (username != null) {
            setWelcomeMessage(username)
            getRanks(username)
        }
    }

    private fun readSharedPref(key: String): String {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val value = sharedPreferences.getString(key, null)

        return value.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun setWelcomeMessage(username: String) {
        binding.tvWelcome.text = "Welcome, \n $username"
    }

    private fun isPlayerLoggedIn() {
        if (readSharedPref("JWT_TOKEN") == "null") {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "pls Log in", Toast.LENGTH_LONG).show()
        }
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "logged out", Toast.LENGTH_LONG).show()
    }

    private fun getRanks(username: String) {
        val headers = HashMap<String, String>()
        headers.put("token", readSharedPref("JWT_TOKEN"))
        callBuilder.buildGetCall("https://gtn-api.herokuapp.com/api/ranks/$username", headers)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("My-Test", "Failed to execute request!")
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val gson = GsonBuilder().create()
                    val getRankResponse = gson.fromJson(body, RanksContainer::class.java)
                    if (getRankResponse != null && !getRankResponse.id.equals(null)) {
                        runOnUiThread {
                            // Stuff that updates the UI
                            binding.tvRankEasy.text = "Easy: ${getRankResponse.rankedEasy}"
                            binding.tvRankMed.text = "Medium: ${getRankResponse.rankedMedium}"
                            binding.tvRankHard.text = "Hard: ${getRankResponse.rankedHard}"
                        }
                    }
                }
            })

    }
}