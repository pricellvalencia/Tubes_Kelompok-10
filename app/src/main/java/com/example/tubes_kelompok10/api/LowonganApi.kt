package com.example.tubes_kelompok10.api

class LowonganApi {
    companion object {
        val BASE_URL = "http://10.113.84.95/androidapi_lowongan/public/api/"

        val register1 = BASE_URL + "register"
        val login = BASE_URL + "login"


        val GET_ALL_URL = BASE_URL + "lowongan/"
        val GET_BY_ID_URL = BASE_URL + "lowongan/"
        val ADD_URL = BASE_URL + "lowongan"
        val UPDATE_URL = BASE_URL + "lowongan/"
        val DELETE_URL = BASE_URL + "lowongan/"

        val GET_ALL_PLMR = BASE_URL + "pelamar/"
        val GET_BY_ID_PLMR = BASE_URL + "pelamar/"
        val ADD_PLMR = BASE_URL + "pelamar"
        val UPDATE_PLMR = BASE_URL + "pelamar/"
        val DELETE_PLMR = BASE_URL + "pelamar/"
    }
}