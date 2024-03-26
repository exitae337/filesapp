package com.example.filesapp.FileApi

import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}