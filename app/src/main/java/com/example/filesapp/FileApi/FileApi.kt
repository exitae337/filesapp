package com.example.filesapp.FileApi

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FileApi {
    @Multipart
    @POST("/uploads/upload-file/{folderName}")
    suspend fun uploadFile(
        @Path("folderName") folderName: String,
        @Part("file") file: MultipartBody.Part
    )

    // Create folder on server
    @POST("/uploads/{folderName}")
    suspend fun createFolder(
        @Path("folderName") folderName: String
    )

    companion object {
        val instance: FileApi by lazy {
            Retrofit.Builder()
                .baseUrl("http://192.168.1.102:8080/") // !Original ip on finish!
                .build()
                .create(FileApi::class.java)
        }
    }
}