package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import com.example.myapplication.R
import com.example.myapplication.data.model.MenuItemEntity
import com.example.myapplication.repository.MenuItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemViewModel @Inject constructor(
    private val repository: MenuItemRepository
) : ViewModel() {

    val menuItems: LiveData<List<MenuItemEntity>> = repository.getAllMenuItems()

    init {
        viewModelScope.launch {
            if (!repository.isExists("Nasi Goreng")) {
                repository.insertMenuItem(MenuItemEntity(
                    nama = "Nasi Goreng",
                    harga = 15000,
                    kategori = "Makanan",
                    imageResId = R.drawable.nasi_goreng
                ))
            }

            if (!repository.isExists("Mie Ayam")) {
                repository.insertMenuItem(MenuItemEntity(
                    nama = "Mie Ayam",
                    harga = 12000,
                    kategori = "Makanan",
                    imageResId = R.drawable.mie_ayam
                ))
            }

            if (!repository.isExists("Es Teh")) {
                repository.insertMenuItem(MenuItemEntity(
                    nama = "Es Teh",
                    harga = 5000,
                    kategori = "Minuman",
                    imageResId = R.drawable.es_teh
                ))
            }
        }
    }



    fun insertMenuItem(item: MenuItemEntity) {
        viewModelScope.launch {
            repository.insertMenuItem(item)
        }
    }
}
