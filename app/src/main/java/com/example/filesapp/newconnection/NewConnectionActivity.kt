package com.example.filesapp.newconnection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.snapshotFlow
import com.example.filesapp.FileApi.FileViewModel
import com.example.filesapp.MainActivity
import com.example.filesapp.databinding.ActivityNewConnectionBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewConnectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewConnectionBinding
    private val fileApiView: FileViewModel by viewModels()
    private lateinit var firebaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)

        binding = ActivityNewConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // For delete Connection from database
        firebaseReference = FirebaseDatabase.getInstance().getReference("listConnections")

        // Progress bar
        val progressBar = binding.newConnectionProgressBar

        // Key to View
        binding.existConnectionTvKSKeyPass.text = intent.getStringExtra("key")

        // Download file
        binding.btnDownloadFile.setOnClickListener {
            val folderName = intent.getStringExtra("key")?.drop(1)
            progressBar.visibility = View.VISIBLE
            runOnUiThread {
                run {
                    fileApiView.downloadFile(folderName!!, applicationContext, progressBar)
                }
            }
        }

        // Delete Connection
        binding.btnDeleteConnection.setOnClickListener {
            // Extras from previous activity
            val firebaseId = intent.getStringExtra("id")!!
            val folderName = intent.getStringExtra("key")?.drop(1)!!
            // Deleting folder from server and node from firebase
            showConfirmationDialog(firebaseId, folderName)
        }

    }

    private fun showConfirmationDialog(firebaseId: String, folderName: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete the connection? " +
                "The file may currently be being transferred to you!")
        builder.setPositiveButton("Yes") { dialog, _ ->
            if(deleteConnection(firebaseId)) {
                fileApiView.deleteConnection(folderName)
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,
                    "Problem with deleting connection: ${intent.getStringExtra("key")!!}",
                    Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            Toast.makeText(this, "Be careful!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // Delete Connection function
    private fun deleteConnection(connectionId: String): Boolean {
        var result = true
        firebaseReference.child(connectionId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Deleted Successfully!",
                    Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { error ->
                result = false
                Toast.makeText(applicationContext, "Failure! Error occurred: $error",
                    Toast.LENGTH_SHORT).show()
            }
        return result
    }

}