package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.myapplication.models.HistoryOrderResponse
import com.example.myapplication.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _historyOrders = mutableStateOf<List<HistoryOrderResponse>>(emptyList())
    val historyOrders: State<List<HistoryOrderResponse>> = _historyOrders

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf("")
    val error: State<String> = _error

    fun loadOrderHistory(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""

            try {
                val response = repository.getOrderHistory(userId)
                if (response.isSuccessful) {
                    _historyOrders.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Gagal memuat riwayat pesanan: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshHistory(userId: Int) {
        loadOrderHistory(userId)
    }
}