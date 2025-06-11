package com.example.myapplication.repository

import com.example.myapplication.models.CategoryRequest
import com.example.myapplication.models.CategoryResponse
import com.example.myapplication.network.RetrofitClient
import retrofit2.Response

class CategoryRepository {

    // Fungsi untuk mendapatkan daftar kategori
    suspend fun getCategories(): Response<List<CategoryResponse>> {
        return RetrofitClient.apiService.getCategories()
    }

    // Fungsi untuk mendapatkan detail kategori berdasarkan ID
    suspend fun getCategoryById(categoryId: Int): Response<CategoryResponse> {
        return RetrofitClient.apiService.getCategoryById(categoryId)
    }

    // Fungsi untuk membuat kategori baru
    suspend fun createCategory(categoryRequest: CategoryRequest): Response<CategoryResponse> {
        return RetrofitClient.apiService.createCategory(categoryRequest)
    }

    // Fungsi untuk memperbarui kategori berdasarkan ID
    suspend fun updateCategory(categoryId: Int, categoryRequest: CategoryRequest): Response<CategoryResponse> {
        return RetrofitClient.apiService.updateCategory(categoryId, categoryRequest)
    }

    // Fungsi untuk menghapus kategori berdasarkan ID
    suspend fun deleteCategory(categoryId: Int): Response<Void> {
        return RetrofitClient.apiService.deleteCategory(categoryId)
    }
}