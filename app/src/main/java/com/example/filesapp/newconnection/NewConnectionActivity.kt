package com.example.filesapp.newconnection

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.filesapp.databinding.ActivityNewConnectionBinding

class NewConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewConnectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)

        binding = ActivityNewConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.existConnectionTvKSKeyPass.text = intent.getStringExtra("key")

    }

}