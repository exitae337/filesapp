package com.example.filesapp.FileApi

import android.content.Context
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
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