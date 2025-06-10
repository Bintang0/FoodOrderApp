package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.data.local.dao.OrderDao
import com.example.myapplication.data.model.OrderEntity
import javax.inject.Inject

class OrderRepository @Inject constructor(private val dao: OrderDao) {
    fun getAllOrders(): LiveData<List<OrderEntity>> = dao.getAllOrders()
    suspend fun insert(order: OrderEntity) = dao.insert(order)
    suspend fun delete(order: OrderEntity) = dao.delete(order)
    suspend fun clearAll() = dao.clearAll()
}
