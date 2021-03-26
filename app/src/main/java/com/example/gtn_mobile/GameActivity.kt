package com.example.gtn_mobile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gtn_mobile.databinding.ActivityGameBinding

class GameActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        readToken()
    }

    private fun readToken() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("JWT_TOKEN", null)
        //just for debugging
        binding.textView3.text = token
        Log.d("My-Test", "retrieve token in Game act: $token")
    }
}