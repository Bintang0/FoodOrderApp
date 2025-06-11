package com.example.myapplication.models

import java.util.Date

data class OrderRequest(
    val id: Int,
    val userId: Int?,
    val total: Int?,
    val date: Date?,
    val status: String?
)

data class OrderResponse(
    val id: Int,
    val userId: Int?,
    val total: Int?,
    val date: Date?,
    val status: String?
)