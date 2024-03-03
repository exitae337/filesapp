package com.example.filesapp.excitingconnection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.withStyledAttributes
import com.example.filesapp.MainActivity
import com.example.filesapp.R
import com.example.filesapp.databinding.ActivityExcitingAuthBinding

class ExcitingAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExcitingAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        binding = ActivityExcitingAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.excitingConnectionButtonKSBackOnMain.setOnClickListener {
            val intent = Intent(this, UploadFileActivity::class.java)
            startActivity(intent)
        }

    }
}