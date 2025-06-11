package com.example.myapplication.repository

import com.example.myapplication.models.OrderRequest
import com.example.myapplication.models.OrderResponse
import com.example.myapplication.models.OrderItemRequest
import com.example.myapplication.models.OrderItemResponse
import com.example.myapplication.network.RetrofitClient
import retrofit2.Response

class OrderRepository {

    // Fungsi untuk mengambil daftar order
    suspend fun getOrders(): Response<List<OrderResponse>> {
        return RetrofitClient.apiService.getOrders()
    }

    // Fungsi untuk mengambil detail order berdasarkan ID
    suspend fun getOrderById(orderId: Int): Response<OrderResponse> {
        return RetrofitClient.apiService.getOrderById(orderId)
    }

    // Fungsi untuk membuat order baru
    suspend fun createOrder(orderRequest: OrderRequest): Response<OrderResponse> {
        return RetrofitClient.apiService.createOrder(orderRequest)
    }

    // Fungsi untuk memperbarui order berdasarkan ID
    suspend fun updateOrder(orderId: Int, orderRequest: OrderRequest): Response<OrderResponse> {
        return RetrofitClient.apiService.updateOrder(orderId, orderRequest)
    }

    // Fungsi untuk menghapus order berdasarkan ID
    suspend fun deleteOrder(orderId: Int): Response<Void> {
        return RetrofitClient.apiService.deleteOrder(orderId)
    }

    // Fungsi untuk mengambil daftar OrderItem berdasarkan Order ID
    suspend fun getOrderItems(orderId: Int): Response<List<OrderItemResponse>> {
        return RetrofitClient.apiService.getOrderItems(orderId)
    }

    // Fungsi untuk menambahkan item ke order
    suspend fun addOrderItem(orderItemRequest: OrderItemRequest): Response<OrderItemResponse> {
        return RetrofitClient.apiService.addOrderItem(orderItemRequest)
    }

    // Fungsi untuk memperbarui item di order berdasarkan ID
    suspend fun updateOrderItem(orderItemId: Int, orderItemRequest: OrderItemRequest): Response<OrderItemResponse> {
        return RetrofitClient.apiService.updateOrderItem(orderItemId, orderItemRequest)
    }

    // Fungsi untuk menghapus item di order berdasarkan ID
    suspend fun deleteOrderItem(orderItemId: Int): Response<Void> {
        return RetrofitClient.apiService.deleteOrderItem(orderItemId)
    }
}