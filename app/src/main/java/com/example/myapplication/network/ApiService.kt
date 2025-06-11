package com.example.myapplication.network

import com.example.myapplication.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Endpoint untuk login pengguna
    @POST("user/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    // Endpoint untuk registrasi pengguna
    @POST("user/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("user/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<UserResponse>

    // Endpoint untuk mendapatkan daftar order
    @GET("order")
    suspend fun getOrders(): Response<List<OrderResponse>>

    // Endpoint untuk mendapatkan history order berdasarkan user ID
    @GET("order/history/{userId}")
    suspend fun getOrderHistory(@Path("userId") userId: Int): Response<List<HistoryOrderResponse>>

    // Endpoint untuk mendapatkan detail order berdasarkan ID
    @GET("order/{id}")
    suspend fun getOrderById(@Path("id") orderId: Int): Response<OrderResponse>

    // Endpoint untuk membuat order baru
    @POST("order")
    suspend fun createOrder(@Body request: OrderRequest): Response<OrderResponse>

    // Endpoint untuk memperbarui order berdasarkan ID
    @PUT("order/{id}")
    suspend fun updateOrder(
        @Path("id") orderId: Int,
        @Body request: OrderRequest
    ): Response<OrderResponse>

    // Endpoint untuk menghapus order berdasarkan ID
    @DELETE("order/{id}")
    suspend fun deleteOrder(@Path("id") orderId: Int): Response<Void>

    // Endpoint untuk mendapatkan daftar OrderItem berdasarkan Order ID
    @GET("orderItem/{id}")
    suspend fun getOrderItems(@Path("id") orderId: Int): Response<List<OrderItemResponse>>

    // Endpoint untuk menambahkan item ke order
    @POST("orderItem")
    suspend fun addOrderItem(@Body request: OrderItemRequest): Response<OrderItemResponse>

    // Endpoint untuk memperbarui item di order berdasarkan ID
    @PUT("orderItem/{id}")
    suspend fun updateOrderItem(
        @Path("id") orderItemId: Int,
        @Body request: OrderItemRequest
    ): Response<OrderItemResponse>

    // Endpoint untuk menghapus item di order berdasarkan ID
    @DELETE("orderItem/{id}")
    suspend fun deleteOrderItem(@Path("id") orderItemId: Int): Response<Void>

    // Endpoint untuk mendapatkan daftar kategori
    @GET("category")
    suspend fun getCategories(): Response<List<CategoryResponse>>

    // Endpoint untuk mendapatkan detail kategori berdasarkan ID
    @GET("category/{id}")
    suspend fun getCategoryById(@Path("id") categoryId: Int): Response<CategoryResponse>

    // Endpoint untuk membuat kategori baru
    @POST("category")
    suspend fun createCategory(@Body request: CategoryRequest): Response<CategoryResponse>

    // Endpoint untuk memperbarui kategori berdasarkan ID
    @PUT("category/{id}")
    suspend fun updateCategory(
        @Path("id") categoryId: Int,
        @Body request: CategoryRequest
    ): Response<CategoryResponse>

    // Endpoint untuk menghapus kategori berdasarkan ID
    @DELETE("category/{id}")
    suspend fun deleteCategory(@Path("id") categoryId: Int): Response<Void>

    // Endpoint untuk mendapatkan daftar food
    @GET("food")
    suspend fun getFoods(): Response<List<FoodResponse>>

    // Endpoint untuk mendapatkan detail food berdasarkan ID
    @GET("food/{id}")
    suspend fun getFoodById(@Path("id") foodId: Int): Response<FoodResponse>

    // Endpoint untuk membuat food baru
    @POST("food")
    suspend fun createFood(@Body request: FoodRequest): Response<FoodResponse>

    // Endpoint untuk memperbarui food berdasarkan ID
    @PUT("food/{id}")
    suspend fun updateFood(
        @Path("id") foodId: Int,
        @Body request: FoodRequest
    ): Response<FoodResponse>

    // Endpoint untuk menghapus food berdasarkan ID
    @DELETE("food/{id}")
    suspend fun deleteFood(@Path("id") foodId: Int): Response<Void>
}