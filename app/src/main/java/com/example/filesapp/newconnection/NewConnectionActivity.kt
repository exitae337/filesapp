package com.example.filesapp.newconnection

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.filesapp.FileApi.FileViewModel
import com.example.filesapp.databinding.ActivityNewConnectionBinding

class NewConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewConnectionBinding
    private val fileApiView: FileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)

        binding = ActivityNewConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressBar = binding.newConnectionProgressBar

        binding.existConnectionTvKSKeyPass.text = intent.getStringExtra("key")

        binding.btnDownloadFile.setOnClickListener {
            val folderName = intent.getStringExtra("key")?.drop(1)
            progressBar.visibility = View.VISIBLE
            runOnUiThread {
                run {
                    fileApiView.downloadFile(folderName!!, applicationContext, progressBar)
                }
            }
        }

    }

}