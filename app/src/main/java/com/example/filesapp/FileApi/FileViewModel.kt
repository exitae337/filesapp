package com.example.filesapp.FileApi

import android.content.Context
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import retrofit2.Callback
import java.io.File

class FileViewModel(
    private val repository: FileRepository = FileRepository()
): ViewModel() {

    // Old version of upload file function
    fun uploadFile(file: File, folderName: String) {
        viewModelScope.launch {
            repository.uploadFile(file, folderName)
        }
    }

    fun uploadFileWithProgress(file: File,
                               folderName: String,
                               progressCallback: (Long, Long) -> Unit) {
        viewModelScope.launch {
            repository.uploadFileWithProgress(file, folderName, progressCallback)
        }
    }

    fun createFolder(folderName: String) {
        viewModelScope.launch {
            repository.createFolder(folderName)
        }
    }
    fun deleteConnection(folderName: String) {
        viewModelScope.launch {
            repository.deleteConnection(folderName)
        }
    }
    fun downloadFile(folderName: String, context: Context, progressBar: ProgressBar){
        viewModelScope.launch {
            repository.downloadFile(folderName, context, progressBar)
        }
    }
}