package com.example.myapplication.repository

import com.example.myapplication.models.FoodRequest
import com.example.myapplication.models.FoodResponse
import com.example.myapplication.network.RetrofitClient
import retrofit2.Response

class FoodRepository {

    // Fungsi untuk mendapatkan daftar food
    suspend fun getFoods(): Response<List<FoodResponse>> {
        return RetrofitClient.apiService.getFoods()
    }

    // Fungsi untuk mendapatkan detail food berdasarkan ID
    suspend fun getFoodById(foodId: Int): Response<FoodResponse> {
        return RetrofitClient.apiService.getFoodById(foodId)
    }

    // Fungsi untuk membuat food baru
    suspend fun createFood(foodRequest: FoodRequest): Response<FoodResponse> {
        return RetrofitClient.apiService.createFood(foodRequest)
    }

    // Fungsi untuk memperbarui food berdasarkan ID
    suspend fun updateFood(foodId: Int, foodRequest: FoodRequest): Response<FoodResponse> {
        return RetrofitClient.apiService.updateFood(foodId, foodRequest)
    }

    // Fungsi untuk menghapus food berdasarkan ID
    suspend fun deleteFood(foodId: Int): Response<Void> {
        return RetrofitClient.apiService.deleteFood(foodId)
    }
}