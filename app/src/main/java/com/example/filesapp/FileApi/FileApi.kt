package com.example.filesapp.FileApi


import androidx.room.Delete
import com.example.filesapp.connection_data.DeleteFolderResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming

interface FileApi {
    // Upload file on Server
    @Multipart
    @POST("/uploads/upload-file/{folderName}")
    suspend fun uploadFile(
        @Path("folderName") folderName: String,
        @Part file: MultipartBody.Part
    )

    // Create folder on server
    @POST("/uploads/{folderName}")
    suspend fun createFolder(
        @Path("folderName") folderName: String
    )

    // Download file from server (folder on server)
    @Streaming
    @GET("/files/download/{folderName}")
    suspend fun downloadFile(
        @Path("folderName") folderName: String
    ): ResponseBody

    @DELETE("/deleteFolder/{folderName}")
    suspend fun deleteConnection (
        @Path("folderName") folderName: String
    )

    @GET("/")
    fun checkConnection():Call<Void>

    companion object {
        val instance: FileApi by lazy {
            Retrofit.Builder()
                .baseUrl("http://192.168.1.102:8080/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(FileApi::class.java)
        }
    }
}
