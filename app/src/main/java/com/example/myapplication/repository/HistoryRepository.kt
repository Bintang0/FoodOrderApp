package com.example.myapplication.repository

import com.example.myapplication.models.HistoryOrderResponse
import com.example.myapplication.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class HistoryRepository @Inject constructor() {

    suspend fun getOrderHistory(userId: Int): Response<List<HistoryOrderResponse>> {
        return RetrofitClient.apiService.getOrderHistory(userId)
    }
}