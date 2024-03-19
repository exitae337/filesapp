package com.example.filesapp.excitingconnection

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filesapp.FileApi.FileViewModel
import com.example.filesapp.R
import com.example.filesapp.databinding.ActivityUploadFileBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class UploadFileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadFileBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        // Flags for fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Set content View
        super.onCreate(savedInstanceState)

        binding = ActivityUploadFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Vars
        val ip = intent.getStringExtra("ip");

        // Button logic
        binding.btnUploadActivityShowIp.setOnClickListener {
            binding.uploadTvShowIp.text = ip.toString()
        }

        // Open bottom dialog
        binding.btnUploadActivityChooseFileToUpload.setOnClickListener {
            UploadFileSheet().show(supportFragmentManager, "newUploadFileTag")
        }

    }

}