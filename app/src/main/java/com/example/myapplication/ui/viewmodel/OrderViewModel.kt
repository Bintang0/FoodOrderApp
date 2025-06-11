package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _syncStatus = MutableLiveData<String>()
    val syncStatus: LiveData<String> = _syncStatus

    fun addOrder(order: OrderEntity) {
        viewModelScope.launch {
            try {
                repository.insert(order)
                _syncStatus.value = "Order berhasil ditambahkan"
            } catch (e: Exception) {
                _error.value = "Gagal menambahkan order: ${e.message}"
            }
        }
    }

    fun addOrderWithSync(menuName: String, quantity: Int, price: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.createOrderWithSync(menuName, quantity, price)
                if (success) {
                    _syncStatus.value = "Order berhasil ditambahkan dan disinkronisasi"
                } else {
                    _syncStatus.value = "Order ditambahkan secara lokal, sinkronisasi gagal"
                }
            } catch (e: Exception) {
                _error.value = "Gagal menambahkan order: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteOrder(order: OrderEntity) {
        viewModelScope.launch {
            try {
                repository.delete(order)
                _syncStatus.value = "Order berhasil dihapus"
            } catch (e: Exception) {
                _error.value = "Gagal menghapus order: ${e.message}"
            }
        }
    }

    fun clearOrders() {
        viewModelScope.launch {
            try {
                repository.clearAll()
                _syncStatus.value = "Semua order berhasil dihapus"
            } catch (e: Exception) {
                _error.value = "Gagal menghapus semua order: ${e.message}"
            }
        }
    }

    fun checkoutOrders(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                // Ubah semua pesanan dengan status kosong atau default menjadi "Menunggu"
                repository.updateStatusForAllPending("Menunggu")
                _syncStatus.value = "Checkout berhasil"
                onSuccess() // panggil callback setelah selesai
            } catch (e: Exception) {
                _error.value = "Gagal checkout: ${e.message}"
            }
        }
    }

    fun syncWithAPI() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.syncOrdersFromAPI()
                if (success) {
                    _syncStatus.value = "Sinkronisasi dengan server berhasil"
                } else {
                    _error.value = "Gagal sinkronisasi dengan server"
                }
            } catch (e: Exception) {
                _error.value = "Gagal sinkronisasi: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fungsi untuk mengisi data dari API berdasarkan Order dan OrderItem models
    fun loadOrdersFromAPI() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ordersResponse = repository.getOrders()
                if (ordersResponse.isSuccessful) {
                    val apiOrders = ordersResponse.body() ?: emptyList()

                    // Clear existing local orders
                    repository.clearAll()

                    // Convert API orders to local OrderEntity
                    apiOrders.forEach { apiOrder ->
                        // Get order items for each order
                        try {
                            val orderItemsResponse = repository.getOrderItems(apiOrder.id)
                            if (orderItemsResponse.isSuccessful) {
                                val orderItems = orderItemsResponse.body() ?: emptyList()

                                // Create local order entities for each order item
                                orderItems.forEach { orderItem ->
                                    val localOrder = OrderEntity(
                                        menuName = "Food ID: ${orderItem.foodId}", // Bisa disesuaikan dengan data food
                                        quantity = orderItem.quantity ?: 1,
                                        price = orderItem.price ?: 0,
                                        status = apiOrder.status ?: "Menunggu",
                                        date = formatDate(apiOrder.date ?: Date())
                                    )
                                    repository.insert(localOrder)
                                }
                            }
                        } catch (e: Exception) {
                            // Jika gagal get order items, buat order default
                            val defaultOrder = OrderEntity(
                                menuName = "Order #${apiOrder.id}",
                                quantity = 1,
                                price = apiOrder.total ?: 0,
                                status = apiOrder.status ?: "Menunggu",
                                date = formatDate(apiOrder.date ?: Date())
                            )
                            repository.insert(defaultOrder)
                        }
                    }
                    _syncStatus.value = "Data berhasil dimuat dari server"
                } else {
                    _error.value = "Gagal memuat data dari server: ${ordersResponse.code()}"
                    loadDummyOrders()
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                loadDummyOrders()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 🧪 Fungsi tambahan untuk mengisi data dummy (fallback)
    private fun loadDummyOrders() {
        viewModelScope.launch {
            val dummyList = listOf(
                OrderEntity(1, "Nasi Goreng Spesial", 2, 30000, "Preparing", "11 Jun, 2025"),
                OrderEntity(2, "Mie Ayam Bakso", 1, 15000, "Completed", "11 Jun, 2025"),
                OrderEntity(3, "Es Teh Manis", 3, 15000, "Menunggu", "11 Jun, 2025"),
                OrderEntity(4, "Ayam Goreng Kremes", 1, 25000, "Completed", "10 Jun, 2025"),
                OrderEntity(5, "Soto Ayam", 1, 18000, "Cancelled", "10 Jun, 2025"),
                OrderEntity(6, "Rendang Daging", 1, 35000, "Preparing", "09 Jun, 2025")
            )
            dummyList.forEach { repository.insert(it) }
            _syncStatus.value = "Data dummy berhasil dimuat"
        }
    }

    fun insertDummyOrders() {
        loadDummyOrders()
    }

    private fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun clearError() {
        _error.value = ""
    }

    fun clearSyncStatus() {
        _syncStatus.value = ""
    }
}