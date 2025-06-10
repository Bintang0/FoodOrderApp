package com.example.myapplication.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.data.model.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("SELECT EXISTS(SELECT 1 FROM categories WHERE name = :name)")
    suspend fun isExists(name: String): Boolean
}