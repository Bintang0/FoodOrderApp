package com.example.myapplication.models

data class OrderItemRequest(
    val id: Int,
    val orderId: Int?,
    val foodId: Int?,
    val quantity: Int?,
    val price: Int?
)

data class OrderItemResponse(
    val id: Int,
    val orderId: Int?,
    val foodId: Int?,
    val quantity: Int?,
    val price: Int?
)