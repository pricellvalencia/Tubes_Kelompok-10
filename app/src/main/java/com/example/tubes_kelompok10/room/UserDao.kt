package com.example.tubes_kelompok10.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user : User)

    @Update
    fun updateUser(user : User)

    @Delete
    suspend fun deleteUser(user : User)

    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM user WHERE id =:user_id")
    fun getUser(user_id: Int): List<User>

    @Query("SELECT * FROM user WHERE username =:username AND password =:password")
    suspend fun getUserLogin(username: String, password: String): User?

    @Query("SELECT * FROM user WHERE username =:username")
    suspend fun getUsername(username: String): User?

}