package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import com.example.myapplication.data.model.OrderEntity
import com.example.myapplication.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    fun checkoutOrders(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Ubah semua pesanan dengan status kosong atau default menjadi "Menunggu"
            repository.updateStatusForAllPending("Menunggu")
            onSuccess() // panggil callback setelah selesai
        }
    }





    // 🧪 Fungsi tambahan untuk mengisi data dummy
    fun insertDummyOrders() {
        viewModelScope.launch {
            val dummyList = listOf(
                OrderEntity(1, "Sausage Delight", 1, 65000, "Preparing", "23 Jun, 2021"),
                OrderEntity(2, "Sausage Delight", 1, 65000, "Completed", "10 Jun, 2021"),
                OrderEntity(3, "Sausage Delight", 1, 65000, "Cancelled", "03 Mar, 2021"),
                OrderEntity(4, "Sausage Delight", 1, 65000, "Completed", "28 Jan, 2021"),
                OrderEntity(5, "Sausage Delight", 1, 65000, "Completed", "24 Dec, 2020"),
                OrderEntity(6, "Sausage Delight", 1, 65000, "Cancelled", "07 Nov, 2020")
            )
            dummyList.forEach { repository.insert(it) }
        }
    }
}
