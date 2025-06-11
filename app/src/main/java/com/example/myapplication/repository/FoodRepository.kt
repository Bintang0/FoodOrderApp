package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.data.local.dao.MenuItemDao
import com.example.myapplication.data.model.MenuItemEntity
import com.example.myapplication.models.FoodRequest
import com.example.myapplication.models.FoodResponse
import com.example.myapplication.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class FoodRepository @Inject constructor(
    private val menuItemDao: MenuItemDao
) {

    // API Methods
    suspend fun getFoods(): Response<List<FoodResponse>> {
        return RetrofitClient.apiService.getFoods()
    }

    suspend fun getFoodById(foodId: Int): Response<FoodResponse> {
        return RetrofitClient.apiService.getFoodById(foodId)
    }

    suspend fun createFood(foodRequest: FoodRequest): Response<FoodResponse> {
        return RetrofitClient.apiService.createFood(foodRequest)
    }

    suspend fun updateFood(foodId: Int, foodRequest: FoodRequest): Response<FoodResponse> {
        return RetrofitClient.apiService.updateFood(foodId, foodRequest)
    }

    suspend fun deleteFood(foodId: Int): Response<Void> {
        return RetrofitClient.apiService.deleteFood(foodId)
    }

    // Local Database Methods
    fun getAllMenuItems(): LiveData<List<MenuItemEntity>> {
        return menuItemDao.getAll()
    }

    suspend fun insertMenuItem(menuItem: MenuItemEntity) {
        menuItemDao.insert(menuItem)
    }

    suspend fun isExists(nama: String): Boolean {
        return menuItemDao.isExists(nama)
    }
}