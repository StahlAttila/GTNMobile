package com.example.gtn_mobile.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gtn_mobile.R
import com.example.gtn_mobile.databinding.ActivityGameBinding
import com.example.gtn_mobile.fragments.EasyGameFragment
import com.example.gtn_mobile.fragments.HardGameFragment
import com.example.gtn_mobile.fragments.InstructionFragment
import com.example.gtn_mobile.fragments.MedGameFragment

class GameActivity : AppCompatActivity(){

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val instructionFragment = InstructionFragment()
        val easyFragment = EasyGameFragment()
        val medFragment = MedGameFragment()
        val hardFragment = HardGameFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.gameFlFragment, instructionFragment)
            commit()
        }

        binding.btnEasy.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.gameFlFragment, easyFragment)
                commit()
            }
        }
        binding.btnMed.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.gameFlFragment, medFragment)
                commit()
            }
        }
        binding.btnHard.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.gameFlFragment, hardFragment)
                commit()
            }
        }
    }

}