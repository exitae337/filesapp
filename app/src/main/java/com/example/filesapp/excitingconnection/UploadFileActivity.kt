package com.example.filesapp.excitingconnection

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.compose.runtime.key
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filesapp.FileApi.FileViewModel
import com.example.filesapp.R
import com.example.filesapp.databinding.ActivityUploadFileBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.File

class UploadFileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadFileBinding
    private val fileApiView: FileViewModel by viewModels()
    private var key: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        // Flags for fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Set content View
        super.onCreate(savedInstanceState)

        binding = ActivityUploadFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Activity vars
        val ip = intent.getStringExtra("ip").toString()
        key = intent.getStringExtra("key").toString()

        // Button logic
        binding.btnUploadActivityShowIp.setOnClickListener {
            binding.uploadTvShowIp.text = ip
        }

        // Open supportFragment to choose file from external storage
        binding.btnUploadActivityChooseFileToUpload.setOnClickListener {
            attachAndUploadAnyFile()
        }

    }

    private fun attachAndUploadAnyFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a file")
                ,100)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Deprecated ("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val uri: Uri? = data.data
            val path: String = uri?.path.toString()
            val file = File(path)
            fileApiView.uploadFile(file, key)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}