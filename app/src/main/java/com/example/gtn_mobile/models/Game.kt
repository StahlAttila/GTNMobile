package com.example.gtn_mobile.models

data class Game(
    var id :Long,
    var difficulty : String,
    var gameType: String,
    var lives : Int,
    var gameStatus : String?,
    var guessDirection: String?,
    var numberToGuess: Int
)
