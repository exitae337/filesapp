package com.example.filesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.filesapp.databinding.ActivityMainBinding
import com.example.filesapp.excitingconnection.ExcitingAuthActivity
import com.example.filesapp.newconnection.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeButtonNewConnection.setOnClickListener {
            val intent = Intent(this, KeySplashActivity::class.java)
            startActivity(intent)
        }

        binding.homeButtonExcitingConnection.setOnClickListener {
            val intent = Intent(this, ExcitingAuthActivity::class.java)
            startActivity(intent)
        }

        binding.homeButtonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}