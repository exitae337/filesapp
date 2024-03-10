package com.example.filesapp.excitingconnection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.withStyledAttributes
import com.example.filesapp.MainActivity
import com.example.filesapp.R
import com.example.filesapp.databinding.ActivityExcitingAuthBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Console
import java.lang.ref.PhantomReference

class ExcitingAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExcitingAuthBinding
    private lateinit var firebaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        binding = ActivityExcitingAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseReference = FirebaseDatabase.getInstance().getReference()
            .child("listConnections")

        binding.newConnectionButtonKSMakeConnection.setOnClickListener {
            if(binding.excitingConnectionEtKSKey.text.isNotEmpty()) {
                val keyStr = binding.excitingConnectionEtKSKey.text
                var booleanConnection = false
                val arrayKeys = mutableListOf<String>()
                val arrayIps = mutableListOf<String>()
                firebaseReference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(dataSnapshot: DataSnapshot in snapshot.children) {
                            arrayKeys.add(dataSnapshot.child("key").value.toString())
                            arrayIps.add(dataSnapshot.child("ip").value.toString())
                        }
                        if (arrayKeys.contains(keyStr.toString())){
                            val index = arrayKeys.indexOf(keyStr.toString())
                            Toast.makeText(applicationContext, "Let's connect!",
                                Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, UploadFileActivity::class.java)
                            intent.putExtra("ip", arrayIps[index])
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext
                                ,"No connection was created with this key!"
                                ,Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, "Error with database is occurred!",
                            Toast.LENGTH_SHORT).show();
                    }
                })
            }else {
                Toast.makeText(applicationContext, "Key value can't be empty!"
                    , Toast.LENGTH_SHORT).show();
            }
        }

    }
}