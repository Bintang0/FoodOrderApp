package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import com.example.myapplication.data.model.OrderEntity
import com.example.myapplication.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    val orders: LiveData<List<OrderEntity>> = repository.getAllOrders()

    fun addOrder(order: OrderEntity) {
        viewModelScope.launch {
            repository.insert(order)
        }
    }

    fun deleteOrder(order: OrderEntity) {
        viewModelScope.launch {
            repository.delete(order)
        }
    }

    fun clearOrders() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}
