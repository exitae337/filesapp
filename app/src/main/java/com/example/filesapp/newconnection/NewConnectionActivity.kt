package com.example.filesapp.newconnection

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.filesapp.FileApi.FileViewModel
import com.example.filesapp.MainActivity
import com.example.filesapp.R
import com.example.filesapp.databinding.ActivityNewConnectionBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

        // Files are ready?
        var filesReady = false
        val firebaseId = intent.getStringExtra("id")!!
        // Path
        val path = firebaseReference.child(firebaseId).child("filesReady")
        path.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val fieldValue = dataSnapshot.getValue(Boolean::class.java)
                if (fieldValue == true) {
                    filesReady = true
                    val circleView = binding.statusCircle
                    circleView.setBackgroundResource(R.drawable.circle_green)
                    Toast.makeText(applicationContext, "Files are ready",
                        Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "Cancelled with Firebase",
                    Toast.LENGTH_SHORT).show()
            }
        })

        // Key to View
        binding.existConnectionTvKSKeyPass.text = intent.getStringExtra("key")

        // Download file
        binding.btnDownloadFile.setOnClickListener {
            if (filesReady) {
                val folderName = intent.getStringExtra("key")?.drop(1)
                progressBar.visibility = View.VISIBLE
                runOnUiThread {
                    run {
                        fileApiView.downloadFile(folderName!!, applicationContext, progressBar)
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Files are not ready to be downloaded!",
                    Toast.LENGTH_SHORT).show()
            }
        }

        // Delete Connection
        binding.btnDeleteConnection.setOnClickListener {
            // Extras from previous activity
            val folderName = intent.getStringExtra("key")?.drop(1)!!
            // Deleting folder from server and node from firebase
            showConfirmationDialog(firebaseId, folderName)
        }



    }

    private fun showConfirmationDialog(firebaseId: String, folderName: String) {
        val builder = AlertDialog.Builder(this)

        // Message and View
        builder.setMessage(R.string.textConfirmations)

        // Positive
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
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
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            Toast.makeText(this, "Be careful!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.Green.toArgb())
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.Red.toArgb())
        }
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