package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.model.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun getAllUsers(): LiveData<List<UserEntity>> = userDao.getAll()

    suspend fun insertUser(user: UserEntity) {
        userDao.insert(user)
    }
    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }
}