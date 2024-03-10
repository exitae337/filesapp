package com.example.filesapp.excitingconnection

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.filesapp.R
import com.example.filesapp.databinding.ActivityUploadFileBinding
import java.io.File

class UploadFileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadFileBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        binding = ActivityUploadFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ip = intent.getStringExtra("ip");

        binding.btnUploadActivityShowIp.setOnClickListener() {
            binding.uploadTvShowIp.text = ip.toString()
        }

    }

    private fun uploadFile(uri: Uri) {

    }
}