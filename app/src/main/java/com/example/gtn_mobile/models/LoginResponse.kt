package com.example.gtn_mobile.models

data class LoginResponse(
    var jwt: String,
    var error: String,
    var username: String,
    var message: String
)