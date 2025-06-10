package com.example.myapplication.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.model.*
import com.example.myapplication.data.local.dao.*

@Database(
    entities = [UserEntity::class, MenuItemEntity::class, OrderEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun menuItemDao(): MenuItemDao
    abstract fun orderDao(): OrderDao
}