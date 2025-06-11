package com.example.myapplication.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val id: Int,
    val name: String,
    val token: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val table_number: String
)

data class RegisterResponse(
    val message: String,
    val id: Int
)