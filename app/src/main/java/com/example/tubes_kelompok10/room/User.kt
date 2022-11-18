package com.example.tubes_kelompok10.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val email: String,
    val phone: Int,
    val tanngalLahir: String,
    val password: String
)