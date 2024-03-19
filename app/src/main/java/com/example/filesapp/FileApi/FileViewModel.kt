package com.example.filesapp.FileApi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel(
    private val repository: FileRepository = FileRepository()
): ViewModel() {
    fun uploadFile(file: File) {
            viewModelScope.launch {
                repository.uploadFile(file)
            }
    }
}