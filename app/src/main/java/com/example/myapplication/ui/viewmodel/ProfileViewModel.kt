package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.UserResponse
import com.example.myapplication.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = mutableStateOf<UserResponse?>(null)
    val user: State<UserResponse?> = _user

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            val response = userRepository.getUserById(userId)
            if (response.isSuccessful) {
                _user.value = response.body()
            }
        }
    }
}