package com.example.filesapp.excitingconnection

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.filesapp.FileApi.FileViewModel
import com.example.filesapp.databinding.ActivityUploadFileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.InputStream

class UploadFileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadFileBinding
    private val fileApiView: FileViewModel by viewModels()
    private lateinit var firebaseReference: DatabaseReference
    private var key: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        // Flags for fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Set content View
        super.onCreate(savedInstanceState)

        binding = ActivityUploadFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Activity vars
        key = intent.getStringExtra("key").toString()
        val id = intent.getStringExtra("id").toString()

        // Open supportFragment to choose file from external storage
        binding.btnUploadActivityChooseFileToUpload.setOnClickListener {
            attachAndUploadAnyFile()
            clearCache()
        }

        // Ready to download button (update child and close @isClickable@ for button)
        binding.btnUploadActivityReady.setOnClickListener {
            val firebaseReference = FirebaseDatabase.getInstance().getReference()
                .child("listConnections").child(id)

            val updates = hashMapOf<String, Any>(
                "filesReady" to true
            )
            // TODO Previous activity starting!
            firebaseReference.updateChildren(updates).addOnSuccessListener {
                Toast.makeText(applicationContext, "Files are ready!",
                    Toast.LENGTH_SHORT).show()
                binding.btnUploadActivityChooseFileToUpload.isClickable = false
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Failure! Error: ${it.message}",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun attachAndUploadAnyFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a file")
                ,100)
        }catch (e: Exception) {
            Toast.makeText(applicationContext, "Error in Start Activity On Result!",
                Toast.LENGTH_SHORT).show()
        }
    }

    // Clear cache directory
    private fun clearCache() {
        try {
            val cacheDir = applicationContext.cacheDir
            if(cacheDir.exists()) {
                val files = cacheDir.listFiles()
                if (files != null) {
                    if (files.isNotEmpty()) {
                        for (file in files) {
                            file.delete()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "Failed to clear cache due to: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("Range")
    @Deprecated ("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val uri: Uri = data.data!!
            val mimeType: String = contentResolver.getType(uri).toString()

            val selectedFile = File(getRealPathFromUri(uri, mimeType))
            val realPath = selectedFile.absolutePath
            if (selectedFile.exists()) {
                try {
                    fileApiView.uploadFileWithProgress(selectedFile, key) { bytesWritten, contentLength ->
                        val progress = ((bytesWritten.toDouble() / contentLength) * 100).toInt()
                        runOnUiThread {
                            binding.progressBarUpload.progress = progress
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Problems with upload!",
                        Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "No such file! $realPath",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Create tmp file to upload on server (with extension)!
    private fun getRealPathFromUri(uri: Uri?, mimeType: String): String {
        if (uri == null) {return ""}
        // Get Extension
        val index = mimeType.lastIndexOf('/')
        val extension = "." + mimeType.drop(index + 1)
        // Save file
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val file = File.createTempFile("temp_file", extension, cacheDir)
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }
}