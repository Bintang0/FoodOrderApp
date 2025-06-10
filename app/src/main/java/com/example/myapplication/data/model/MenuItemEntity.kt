package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val harga: Int,
    val kategori: String,
    val imageResId: Int // ID dari drawable resource
)
