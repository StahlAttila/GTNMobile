package com.example.gtn_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gtn_mobile.databinding.ActivitySignupBinding
import com.example.gtn_mobile.httprequests.HttpRequestCallBuilder
import com.example.gtn_mobile.models.SignupResponse
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val callBuilder = HttpRequestCallBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvSignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
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

            signUp(username, email, password)
        }

    }

    private fun signUp(username: String, email: String, password: String) {
        val url = "https://gtn-api.herokuapp.com/sign-up"
        val params = HashMap<String, String>()
        params.put("username", username)
        params.put("email", email)
        params.put("password", password)

        callBuilder.buildPostCall(url, null, params).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("My-Test", "Failed to send signup request.")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val signpUpResponse = gson.fromJson(body, SignupResponse::class.java)

                if(!signpUpResponse.id.equals(null)) {
                    goToSignin()
                }
            }
        })

    }

    private  fun goToSignin() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}