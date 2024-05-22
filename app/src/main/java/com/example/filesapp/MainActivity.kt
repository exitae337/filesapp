package com.example.filesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.example.filesapp.FileApi.FileRepository
import com.example.filesapp.FileApi.FileViewModel
import com.example.filesapp.databinding.ActivityMainBinding
import com.example.filesapp.excitingconnection.ExcitingAuthActivity
import com.example.filesapp.newconnection.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fileApiView: FileViewModel by viewModels()
    private val repository: FileRepository = FileRepository()

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressBar = binding.mainProgressBar

        binding.homeButtonNewConnection.setOnClickListener {
            Toast.makeText(applicationContext, "Trying to access the server...",
                Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.VISIBLE
            repository.checkServerAvailability(progressBar) {
                if (it) {
                    Toast.makeText(applicationContext, "Good!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, KeySplashActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Server is not responding!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.homeButtonExcitingConnection.setOnClickListener {
            Toast.makeText(applicationContext, "Trying to access the server...",
                Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.VISIBLE
            repository.checkServerAvailability(progressBar) {
                if(it) {
                    Toast.makeText(applicationContext, "Good!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ExcitingAuthActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Server is not responding!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.homeButtonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}