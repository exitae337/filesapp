package com.example.filesapp.newconnection


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.lifecycleScope
import com.example.filesapp.MainActivity
import com.example.filesapp.databinding.ActivityKeySplashBinding
import com.example.filesapp.connection_data.Connection
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.lang.ref.PhantomReference
import java.util.Formatter
import kotlin.random.Random


class KeySplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeySplashBinding
    private lateinit var firebaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        binding = ActivityKeySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseReference = FirebaseDatabase.getInstance().getReference("listConnections")

        binding.newConnectionTvKSKeyPass.text = generateRandomKey()

        //Copy key to ClipBoard
        val keyConnectionString = binding.newConnectionTvKSKeyPass
        keyConnectionString.setOnClickListener {
            val str = binding.newConnectionTvKSKeyPass.text.toString()

            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("key", str)
            clipboard.setPrimaryClip(clip)
        }

        //Back on main
        val buttonBackOnMain = binding.newConnectionButtonKSBackOnMain
        buttonBackOnMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Start new connection
        val buttonNewConnection = binding.newConnectionButtonKSMakeConnection
        buttonNewConnection.setOnClickListener {
            val keyPas = binding.newConnectionTvKSKeyPass.text.toString()
            val yourIp = getIpAddress()

            if (yourIp != null) {
                saveData(keyPas, yourIp)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }


    }

    private fun generateRandomKey(): String {
        val sb = StringBuilder(5)
        val alphabet = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val rand = Random

        for(i in 0 until sb.capacity()) {
            val index = rand.nextInt(alphabet.length)
            sb.append(alphabet[index])
        }

        return "#$sb"
    }

    private fun saveData(keyPas: String, yourIp: String) {
        val port: Int = 5555
        val connectionId = firebaseReference.push().key!!

        val connection = Connection(keyPas, yourIp, port)

        firebaseReference.child(connectionId).setValue(connection)
            .addOnCompleteListener {
                Toast.makeText(applicationContext, "Completed!", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed! ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun ipToString(i: Int): String {
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)
    }

    private fun getIpAddress(): String? {
        try {
            val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            return ipToString(wifiManager.connectionInfo.ipAddress)
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Can't get your IP!", Toast.LENGTH_SHORT)
                .show()
        }

        return null
    }
}