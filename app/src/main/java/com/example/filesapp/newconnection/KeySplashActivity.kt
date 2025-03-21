package com.example.filesapp.newconnection


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.filesapp.FileApi.FileViewModel
import com.example.filesapp.MainActivity
import com.example.filesapp.databinding.ActivityKeySplashBinding
import com.example.filesapp.connection_data.Connection
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random


@SuppressLint("CustomSplashScreen")
class KeySplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeySplashBinding
    private lateinit var firebaseReference: DatabaseReference
    private val fileApiView: FileViewModel by viewModels()

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
            fileApiView.createFolder(keyPas)
            val firebaseId = saveData(keyPas, false)
            val intent = Intent(this, NewConnectionActivity::class.java)
            intent.putExtra("key", keyPas)
            intent.putExtra("id", firebaseId)
            startActivity(intent)
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

    private fun saveData(keyPas: String, ready: Boolean): String {

        val connectionId = firebaseReference.push().key!!
        val connection = Connection(keyPas, true, ready)

        firebaseReference.child(connectionId).setValue(connection)
            .addOnCompleteListener {
                Toast.makeText(applicationContext, "Completed!", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed! ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        return connectionId
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