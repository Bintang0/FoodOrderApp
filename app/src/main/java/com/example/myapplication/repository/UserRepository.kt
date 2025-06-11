package com.example.myapplication.repository

import com.example.myapplication.models.RegisterRequest
import com.example.myapplication.models.RegisterResponse
import com.example.myapplication.models.LoginRequest
import com.example.myapplication.models.LoginResponse
import com.example.myapplication.network.RetrofitClient
import retrofit2.Response

class UserRepository {

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val request = LoginRequest(email, password)
        return RetrofitClient.apiService.loginUser(request)
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        table_number: String
    ): Response<RegisterResponse> {
        val request = RegisterRequest(name, email, password, table_number)
        return RetrofitClient.apiService.registerUser(request)
    }
}