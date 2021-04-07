package com.example.gtn_mobile.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gtn_mobile.R
import com.example.gtn_mobile.databinding.FragmentEasyGameBinding
import com.example.gtn_mobile.httprequests.HttpRequestCallBuilder
import com.example.gtn_mobile.models.Game
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class EasyGameFragment : Fragment(R.layout.fragment_easy_game) {

    private var _binding: FragmentEasyGameBinding? = null
    private val binding get() = _binding!!
    private val callBuilder = HttpRequestCallBuilder()
    private var id: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEasyGameBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newGame()

        binding.etGuess.setOnClickListener {
            binding.etGuess.setText("")
        }

        binding.btnGuess.setOnClickListener {
            if (binding.btnGuess.text.equals("NEW GAME")) {
                newGame()
            }

            if(!binding.etGuess.text.isNullOrBlank()) {
                val guess = Integer.valueOf(binding.etGuess.text.toString())
                makeGuess(id, guess)
            }
        }
    }

    private fun newGame() {
        binding.etGuess.setText("")
        val url = "https://gtn-api.herokuapp.com/api/game/new"

        val headers = HashMap<String, String>()
        val params = HashMap<String, String>()
        headers["token"] = readSharedPref("JWT_TOKEN")
        params["gameType"] = "RANKED"
        params["difficulty"] = "EASY"
        callBuilder.buildPostCall(url, headers, params).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("My-Test", "Could send new game request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val gameResponse = gson.fromJson(body, Game::class.java)

                updateViews(gameResponse)
            }
        })
    }

    private fun makeGuess(id: Long, guess: Int) {
        val url = "https://gtn-api.herokuapp.com/api/game/guess"
        val headers = HashMap<String, String>()
        headers["token"] = readSharedPref("JWT_TOKEN")
        val params = HashMap<String, String>()
        params["gameId"] = id.toString()
        params["guess"] = guess.toString()

        callBuilder.buildPostCall(url, headers, params).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("My-Test", "couldnt send guess request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val gameResponse = gson.fromJson(body, Game::class.java)

                updateViews(gameResponse)
            }
        })
    }

    private fun readSharedPref(key: String): String {
        val sharedPreferences =
            this.activity?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val value = sharedPreferences?.getString(key, null)
        return value.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun updateViews(gameResponse: Game) {
        id = gameResponse.id
        activity?.runOnUiThread {
            // Stuff that updates the UI
            binding.tvEasyId.text = "id: " + gameResponse.id
            binding.tvEasyLives.text =
                "lives: " + gameResponse.lives

            if (gameResponse.guessDirection == null) {
                binding.tvEasyDirection.visibility = View.INVISIBLE
            } else {
                binding.tvEasyDirection.visibility = View.VISIBLE
                binding.tvEasyDirection.text = gameResponse.guessDirection
            }

            if (gameResponse.gameStatus == null) {
                binding.tvEasyStatus.visibility = View.INVISIBLE
                binding.btnGuess.text = "GUESS"
            } else {
                binding.tvEasyStatus.visibility = View.VISIBLE
                binding.tvEasyStatus.text = "status: " + gameResponse.gameStatus
                binding.tvEasyDirection.text = "It was " + gameResponse.numberToGuess.toString()
                binding.btnGuess.text = "NEW GAME"
            }
        }
    }


}