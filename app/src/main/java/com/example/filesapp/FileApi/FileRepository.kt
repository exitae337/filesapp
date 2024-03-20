package com.example.filesapp.FileApi

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class FileRepository {
    suspend fun uploadFile(file: File, folderName: String): Boolean{
        return try {
            FileApi.instance.uploadFile(
                folderName,
                file = MultipartBody.Part
                    .createFormData(
                        "file",
                        file.name,
                        file.asRequestBody()
                    )
            )
            true
        }catch (e: IOException){
            e.printStackTrace()
            false
        }
        // TODO HttpException analyse with catch block
    }
}