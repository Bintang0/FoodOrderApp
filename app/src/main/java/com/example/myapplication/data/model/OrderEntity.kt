package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val menuName: String,
    val quantity: Int,
    val price: Int,
    val status: String = "Menunggu", // contoh default status
    val date: String // format: "10 Juni 2025"
)

