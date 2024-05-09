package com.example.filesapp.FileApi

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel(
    private val repository: FileRepository = FileRepository()
): ViewModel() {
    fun uploadFile(file: File, folderName: String) {
        viewModelScope.launch {
            repository.uploadFile(file, folderName)
        }
    }
    fun createFolder(folderName: String) {
        viewModelScope.launch {
            repository.createFolder(folderName)
        }
    }

    fun downloadFile(folderName: String, context: Context, progressBar: ProgressBar){
        val job = viewModelScope.launch {
            repository.downloadFile(folderName, context)
        }
        job.invokeOnCompletion { throwable ->
            if (throwable != null) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Failure!",
                    Toast.LENGTH_SHORT).show()
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "All good!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}