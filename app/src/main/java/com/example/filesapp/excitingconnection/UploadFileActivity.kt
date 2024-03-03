package com.example.filesapp.excitingconnection

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.filesapp.R
import com.example.filesapp.databinding.ActivityUploadFileBinding
import java.io.File

class UploadFileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadFileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uploadFile.setOnClickListener() {

        }

    }

    private fun uploadFile(uri: Uri) {

    }
}