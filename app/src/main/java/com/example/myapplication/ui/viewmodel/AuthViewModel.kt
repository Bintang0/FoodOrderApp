package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.LoginResponse
import com.example.myapplication.models.RegisterResponse
import com.example.myapplication.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = UserRepository()

    // State untuk menyimpan hasil login, register dan error message
    val loginResult = mutableStateOf<LoginResponse?>(null)
    val registerResult = mutableStateOf<RegisterResponse?>(null)
    val errorMessage = mutableStateOf("")

    // Fungsi untuk login
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                if (response.isSuccessful) {
                    loginResult.value = response.body()
                } else {
                    errorMessage.value = "Login gagal: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    // Fungsi untuk register
    fun register(name: String, email: String, password: String, table_number: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(name, email, password, table_number)
                if (response.isSuccessful) {
                    registerResult.value = response.body()
                } else {
                    errorMessage.value = "Register gagal: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    // Fungsi untuk mereset registerResult
    fun clearRegisterResult() {
        registerResult.value = null
    }
}