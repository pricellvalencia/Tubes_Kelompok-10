package com.example.tubes_kelompok10.api

class LowonganApi {
    companion object {
        val BASE_URL = "https://tubes-kelompok10-default-rtdb.firebaseio.com"

        val GET_ALL_URL = BASE_URL + "lowongan/"
        val GET_BY_ID_URL = BASE_URL + "lowongan/"
        val ADD_URL = BASE_URL + "lowongan"
        val UPDATE_URL = BASE_URL + "lowongan/"
        val DELETE_URL = BASE_URL + "lowongan/"
    }
}