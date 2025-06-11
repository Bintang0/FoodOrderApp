package com.example.myapplication.models

data class CategoryRequest(
    val id: Int,
    val name: String
)

data class CategoryResponse(
    val id: Int,
    val name: String
)