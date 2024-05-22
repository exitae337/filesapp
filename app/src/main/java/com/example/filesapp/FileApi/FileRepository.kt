package com.example.filesapp.FileApi

import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.filesapp.connection_data.DeleteFolderResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class FileRepository {
    suspend fun uploadFile(file: File, folderName: String): Boolean{
        return try {
            FileApi.instance.uploadFile(
                folderName.drop(1),
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

    suspend fun createFolder(folderName: String): Boolean {
        return try {
            FileApi.instance.createFolder(folderName)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteConnection(folderName: String): Boolean {
        return try {
            FileApi.instance.deleteConnection(folderName)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun checkServerAvailability(progressBar: ProgressBar, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = FileApi.instance.checkConnection().execute()
                val isServerAvailable = response.isSuccessful
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    callback(isServerAvailable)
                }
            } catch (e1: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    callback(false)
                }
            }
            catch (e2: UnknownHostException) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    callback(false)
                }
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun downloadFile(folderName: String, context: Context, progressBar: ProgressBar){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val responseBody = FileApi.instance.downloadFile(folderName)
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val resultFile = File(downloadsDir, folderName)
                responseBody.byteStream().use { inputStream ->
                    resultFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Completed!",
                        Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failure + ${e.printStackTrace()}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
