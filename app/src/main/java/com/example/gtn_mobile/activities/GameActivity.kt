package com.example.gtn_mobile.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gtn_mobile.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity(){

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnEasy.setOnClickListener { {} }
        binding.btnMed.setOnClickListener { {} }
        binding.btnHard.setOnClickListener { {} }
    }

}