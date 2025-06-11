package com.example.myapplication.models

data class FoodRequest(
    val id: Int,
    val name: String?,
    val price: Int?,
    val stock: Int?,
    val categoryId: Int?
)

data class FoodResponse(
    val id: Int,
    val name: String?,
    val price: Int?,
    val stock: Int?,
    val categoryId: Int?
)