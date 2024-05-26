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
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.wait
import okio.buffer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Url
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.NoConnectionPendingException

class FileRepository {

    interface ApiService {
        @Multipart
        @POST("/uploads/upload-file/{folderName}")
        suspend fun uploadFile(
            @Path("folderName") folderName: String,
            @Part file: MultipartBody.Part
        )
    }

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
        } catch (e: IOException) {
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

    suspend fun uploadFileWithProgress(file: File,
                                       folderName:String,
                                       progressCallback: (Long, Long) -> Unit) {

        val body = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody())
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method, ProgressRequestBody(original.body!!) {
                        bytesWritten, contentLength ->
                        run {
                            progressCallback(bytesWritten, contentLength)
                        }
                    }).build()
                chain.proceed(request)
            }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://89.179.73.25:80/")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        retrofit.uploadFile(folderName.drop(1), body)

    }

    class ProgressRequestBody(
        private val requestBody: RequestBody,
        private val progressCallback: (Long, Long) -> Unit
    ) : RequestBody() {

        override fun contentType(): okhttp3.MediaType? {
            return requestBody.contentType()
        }

        override fun contentLength(): Long {
            return requestBody.contentLength()
        }

        override fun writeTo(sink: okio.BufferedSink) {
            val progressSink = object : okio.ForwardingSink(sink) {
                var bytesWritten = 0L
                var totalBytes = 0L

                override fun write(source: okio.Buffer, byteCount: Long) {
                    super.write(source, byteCount)
                    bytesWritten += byteCount
                    if (totalBytes == 0L) {
                        totalBytes = contentLength()
                    }
                    progressCallback(bytesWritten, totalBytes)
                }
            }
            val countingSink = progressSink.buffer()
            requestBody.writeTo(countingSink)
            countingSink.flush()
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
            catch (e3: NoConnectionPendingException) {
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
