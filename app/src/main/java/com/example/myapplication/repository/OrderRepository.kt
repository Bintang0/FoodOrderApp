package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.data.local.dao.OrderDao
import com.example.myapplication.data.model.OrderEntity
import com.example.myapplication.models.OrderRequest
import com.example.myapplication.models.OrderResponse
import com.example.myapplication.models.OrderItemRequest
import com.example.myapplication.models.OrderItemResponse
import com.example.myapplication.network.RetrofitClient
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderDao: OrderDao
) {

    // Local Database Methods
    fun getAllOrders(): LiveData<List<OrderEntity>> {
        return orderDao.getAllOrders()
    }

    suspend fun insert(order: OrderEntity) {
        orderDao.insert(order)
    }

    suspend fun delete(order: OrderEntity) {
        orderDao.delete(order)
    }

    suspend fun clearAll() {
        orderDao.clearAll()
    }

    suspend fun updateStatusForAllPending(newStatus: String) {
        orderDao.updateStatusForAllPending(newStatus)
    }

    // API Methods
    suspend fun getOrders(): Response<List<OrderResponse>> {
        return RetrofitClient.apiService.getOrders()
    }

    suspend fun getOrderById(orderId: Int): Response<OrderResponse> {
        return RetrofitClient.apiService.getOrderById(orderId)
    }

    suspend fun createOrder(orderRequest: OrderRequest): Response<OrderResponse> {
        return RetrofitClient.apiService.createOrder(orderRequest)
    }

    suspend fun updateOrder(orderId: Int, orderRequest: OrderRequest): Response<OrderResponse> {
        return RetrofitClient.apiService.updateOrder(orderId, orderRequest)
    }

    suspend fun deleteOrder(orderId: Int): Response<Void> {
        return RetrofitClient.apiService.deleteOrder(orderId)
    }

    suspend fun getOrderItems(orderId: Int): Response<List<OrderItemResponse>> {
        return RetrofitClient.apiService.getOrderItems(orderId)
    }

    suspend fun addOrderItem(orderItemRequest: OrderItemRequest): Response<OrderItemResponse> {
        return RetrofitClient.apiService.addOrderItem(orderItemRequest)
    }

    suspend fun updateOrderItem(orderItemId: Int, orderItemRequest: OrderItemRequest): Response<OrderItemResponse> {
        return RetrofitClient.apiService.updateOrderItem(orderItemId, orderItemRequest)
    }

    suspend fun deleteOrderItem(orderItemId: Int): Response<Void> {
        return RetrofitClient.apiService.deleteOrderItem(orderItemId)
    }

    // Hybrid methods that sync with API and local database
    suspend fun syncOrdersFromAPI(): Boolean {
        return try {
            val response = getOrders()
            if (response.isSuccessful) {
                val apiOrders = response.body() ?: emptyList()
                val localOrders = apiOrders.map { apiOrder ->
                    OrderEntity(
                        id = apiOrder.id,
                        menuName = "Order #${apiOrder.id}", // Default name, bisa disesuaikan
                        quantity = 1, // Default quantity
                        price = apiOrder.total ?: 0,
                        status = apiOrder.status ?: "Menunggu",
                        date = formatDate(apiOrder.date ?: Date())
                    )
                }
                // Insert ke local database
                localOrders.forEach { order ->
                    insert(order)
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun createOrderWithSync(menuName: String, quantity: Int, price: Int): Boolean {
        return try {
            // Simpan ke local database terlebih dahulu
            val localOrder = OrderEntity(
                menuName = menuName,
                quantity = quantity,
                price = price,
                status = "Menunggu",
                date = getCurrentFormattedDate()
            )
            insert(localOrder)

            // Sync ke API
            val apiOrder = OrderRequest(
                id = 0, // Auto generate
                userId = 1, // Default user ID, bisa disesuaikan
                total = price * quantity,
                date = Date(),
                status = "Menunggu"
            )
            val response = createOrder(apiOrder)
            response.isSuccessful
        } catch (e: Exception) {
            // Meskipun API gagal, local order tetap tersimpan
            true // Return true karena local save berhasil
        }
    }

    private fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    private fun getCurrentFormattedDate(): String {
        val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return formatter.format(Date())
    }
}