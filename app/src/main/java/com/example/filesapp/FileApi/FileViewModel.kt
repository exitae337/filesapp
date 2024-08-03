package com.example.filesapp.FileApi

import android.content.Context
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel(
    private val repository: FileRepository = FileRepository()
): ViewModel() {
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