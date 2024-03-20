package com.example.filesapp.FileApi

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApi {
    @Multipart
    @POST("/uploads/upload-file")
    suspend fun uploadFile(
        @Part("folder_name") folderName: String,
        @Part file: MultipartBody.Part
    )

    companion object {
        val instance: FileApi by lazy {
            Retrofit.Builder()
                .baseUrl("http://192.168.1.102:8080/")
                .build()
                .create(FileApi::class.java)
        }
    }
}