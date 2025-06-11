package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.data.model.MenuItemEntity
import com.example.myapplication.repository.FoodRepository
import com.example.myapplication.models.FoodResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _menuItems = MutableLiveData<List<MenuItemEntity>>()
    val menuItems: LiveData<List<MenuItemEntity>> = _menuItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadFoodsFromAPI()
    }

    private fun loadFoodsFromAPI() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getFoods()
                if (response.isSuccessful) {
                    val foods = response.body() ?: emptyList()
                    val menuItems = foods.map { food ->
                        MenuItemEntity(
                            id = food.id,
                            nama = food.name ?: "Unknown Food",
                            harga = food.price ?: 0,
                            kategori = getCategoryName(food.categoryId ?: 0),
                            imageResId = getImageResource(food.name ?: "")
                        )
                    }
                    _menuItems.value = menuItems

                    // Insert to local database
                    menuItems.forEach { item ->
                        if (!repository.isExists(item.nama)) {
                            repository.insertMenuItem(item)
                        }
                    }
                } else {
                    _error.value = "Failed to load foods: ${response.code()}"
                    // Fallback to local data or dummy data
                    loadDummyData()
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                // Fallback to local data or dummy data
                loadDummyData()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadDummyData() {
        viewModelScope.launch {
            val dummyItems = listOf(
                MenuItemEntity(
                    nama = "Nasi Goreng",
                    harga = 15000,
                    kategori = "Makanan",
                    imageResId = R.drawable.nasi_goreng
                ),
                MenuItemEntity(
                    nama = "Mie Ayam",
                    harga = 12000,
                    kategori = "Makanan",
                    imageResId = R.drawable.mie_ayam
                ),
                MenuItemEntity(
                    nama = "Es Teh",
                    harga = 5000,
                    kategori = "Minuman",
                    imageResId = R.drawable.es_teh
                ),
                MenuItemEntity(
                    nama = "Ayam Goreng",
                    harga = 18000,
                    kategori = "Makanan",
                    imageResId = R.drawable.mie_ayam
                ),
                MenuItemEntity(
                    nama = "Jus Jeruk",
                    harga = 8000,
                    kategori = "Minuman",
                    imageResId = R.drawable.es_teh
                )
            )

            _menuItems.value = dummyItems

            // Insert dummy data to local database
            dummyItems.forEach { item ->
                if (!repository.isExists(item.nama)) {
                    repository.insertMenuItem(item)
                }
            }
        }
    }

    private fun getCategoryName(categoryId: Int): String {
        return when (categoryId) {
            1 -> "Makanan"
            2 -> "Minuman"
            3 -> "Snack"
            4 -> "Dessert"
            else -> "Lainnya"
        }
    }

    private fun getImageResource(foodName: String): Int {
        return when (foodName.lowercase()) {
            "nasi goreng" -> R.drawable.nasi_goreng
            "mie ayam" -> R.drawable.mie_ayam
            "es teh" -> R.drawable.es_teh
            else -> R.drawable.es_teh // Pastikan ada default image
        }
    }

    fun insertMenuItem(item: MenuItemEntity) {
        viewModelScope.launch {
            repository.insertMenuItem(item)
        }
    }

    fun refreshData() {
        loadFoodsFromAPI()
    }
}