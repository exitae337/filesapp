package com.example.filesapp.newconnection

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.filesapp.databinding.ActivityNewConnectionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.Socket
import java.util.Scanner

class NewConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewConnectionBinding;

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)

        binding = ActivityNewConnectionBinding.inflate(layoutInflater);
        setContentView(binding.root);

    }

}