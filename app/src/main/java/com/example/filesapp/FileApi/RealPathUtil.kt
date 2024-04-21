package com.example.filesapp.FileApi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class FilePicker(private val activity: AppCompatActivity) {
    @SuppressLint("Range")
    private val pickFile = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val uri: Uri = data.data!!

                val realPath = if (DocumentsContract.isDocumentUri(activity, uri)) {
                    val cursor = activity.contentResolver.query(uri, null, null, null, null)
                    cursor.use {
                        it!!.moveToFirst()
                        it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } else {
                    File(uri.path!!).name
                }

                Log.d("com.example.filesapp.FileApi.FilePicker", "Real Path: $realPath")
            }
        }
    }

    fun pickFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        pickFile.launch(intent)
    }
}