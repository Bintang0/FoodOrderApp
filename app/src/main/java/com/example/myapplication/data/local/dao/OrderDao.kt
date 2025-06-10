package com.example.myapplication.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.data.model.OrderEntity

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity)

    @Query("SELECT * FROM orders")
    fun getAllOrders(): LiveData<List<OrderEntity>>

    @Delete
    suspend fun delete(order: OrderEntity)

    @Query("DELETE FROM orders")
    suspend fun clearAll()

    @Query("UPDATE orders SET status = :newStatus WHERE status = 'Menunggu' OR status = '' OR status IS NULL")
    suspend fun updateStatusForAllPending(newStatus: String)


}
