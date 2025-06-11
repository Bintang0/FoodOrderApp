package com.example.myapplication.models

import java.util.Date

data class HistoryOrderResponse(
    val id: Int,
    val userId: Int?,
    val total: Int?,
    val date: Date?,
    val status: String?,
    val orderItems: List<HistoryOrderItemResponse>?
)

data class HistoryOrderItemResponse(
    val id: Int,
    val orderId: Int?,
    val foodId: Int?,
    val foodName: String?,
    val quantity: Int?,
    val price: Int?
)

data class HistoryOrderRequest(
    val userId: Int
)