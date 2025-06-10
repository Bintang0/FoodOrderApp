package com.example.myapplication.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.data.model.MenuItemEntity

@Dao
interface MenuItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(menuItem: MenuItemEntity)

    @Query("SELECT * FROM menu_items")
    fun getAll(): LiveData<List<MenuItemEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM menu_items WHERE nama = :nama)")
    suspend fun isExists(nama: String): Boolean

}
