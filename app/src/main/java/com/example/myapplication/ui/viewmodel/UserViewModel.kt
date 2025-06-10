package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.model.UserEntity
import com.example.myapplication.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    val users: LiveData<List<UserEntity>> = repository.getAllUsers()

    fun login(email: String, password: String): Boolean {
        // Dummy login
        return email == "admin@example.com" && password == "123456"
    }

    suspend fun register(name: String, email: String, password: String): Boolean {
        if (name.isBlank() || email.isBlank() || password.isBlank()) return false

        // Pastikan panggilan suspend dijalankan di IO thread
        val existingUser = withContext(Dispatchers.IO) {
            repository.getUserByEmail(email)
        }

        if (existingUser != null) return false

        val newUser = UserEntity(nama = name, email = email, password = password)
        withContext(Dispatchers.IO) {
            repository.insertUser(newUser)
        }

        return true
    }



    fun insertUser(user: UserEntity) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }
}