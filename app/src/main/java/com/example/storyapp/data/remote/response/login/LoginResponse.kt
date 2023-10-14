package com.example.storyapp.data.remote.response.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("loginResult")
    val loginResult: LoginResult? = null,
    @SerializedName("message")
    val message: String
)