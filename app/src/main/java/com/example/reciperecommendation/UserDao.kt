package com.example.reciperecommendation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE lower(username) = lower(:username) LIMIT 1")
    suspend fun getByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE is_logged_in = 1 LIMIT 1")
    suspend fun getLoggedInUser(): UserEntity?

    @Query("UPDATE users SET is_logged_in = 0")
    suspend fun logoutAll()

    @Query("UPDATE users SET is_logged_in = 1 WHERE id = :userId")
    suspend fun setLoggedIn(userId: Long)
}
