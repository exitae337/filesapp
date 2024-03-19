package com.example.filesapp.FileApi

import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.http.Multipart
import retrofit2.http.Part

interface FileApi {
    @Multipart
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    )

    companion object {
        val instance by lazy {
            Retrofit.Builder()
                .baseUrl("http://192.168.1.102:8080/uploads")
                .build()
                .create(FileApi::class.java)
        }
    }
}