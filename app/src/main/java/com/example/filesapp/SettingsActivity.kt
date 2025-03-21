package com.example.filesapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.filesapp.databinding.ActivitySettingsBinding
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var switchTheme: SwitchMaterial
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // For theme change
        // Dark mode
        binding.settingsButtonDarkMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            saveTheme(true)
            Toast.makeText(applicationContext, "Night mode", Toast.LENGTH_SHORT).show()
        }

        //Light mode
        binding.settingsButtonLightMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            saveTheme(false)
            Toast.makeText(applicationContext, "Light mode", Toast.LENGTH_SHORT).show()
        }

        // Button back on Main
        binding.settingsButtonKSBackOnMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnSendMail.setOnClickListener {
            if(binding.contactThemeText.text.isEmpty()) {
                Toast.makeText(applicationContext,
                    "Theme can't be empty!",
                    Toast.LENGTH_SHORT).show()
            } else if (binding.contactMailText.text.isEmpty()) {
                Toast.makeText(applicationContext,
                    "Text of the e-mail can't be empty!",
                    Toast.LENGTH_SHORT).show()
            } else {
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.type = "plain/text"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(
                    "exitae337@gmail.com"
                ))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                    binding.contactThemeText.text.toString())
                emailIntent.putExtra(Intent.EXTRA_TEXT,
                    binding.contactMailText.text.toString())

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send message"))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(applicationContext,
                        "You don't have an email client on your device...",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun saveTheme(theme: Boolean) {
        val sharedPreferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("selected_theme", theme)
        editor.apply()
    }
}