package com.example.gtn_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gtn_mobile.databinding.ActivityMainBinding
import com.example.gtn_mobile.databinding.ActivitySignupBinding
import com.example.gtn_mobile.httprequests.HttpRequestCallBuilder

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val callBuilder = HttpRequestCallBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvSignin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btSignup.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPw = binding.etConfirmPassword.text.toString()
            if(password != confirmPw) {
                Toast.makeText(this, "Password doesnt match", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            signUp(username, email, password, confirmPw)
        }

    }

    private fun signUp(username: String, email: String, password: String, confirmPw: String) {
        val url = "https://gtn-api.herokuapp.com/sign-up"
    }
}