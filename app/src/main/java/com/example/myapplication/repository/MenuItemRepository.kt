package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.data.local.dao.MenuItemDao
import com.example.myapplication.data.model.MenuItemEntity
import javax.inject.Inject

class MenuItemRepository @Inject constructor(
    private val menuItemDao: MenuItemDao
) {
    fun getAllMenuItems(): LiveData<List<MenuItemEntity>> = menuItemDao.getAll()

    suspend fun insertMenuItem(menuItem: MenuItemEntity) {
        menuItemDao.insert(menuItem)
    }
    suspend fun isExists(nama: String): Boolean {
        return menuItemDao.isExists(nama)
    }


}