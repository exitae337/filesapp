package com.example.filesapp.excitingconnection

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.filesapp.FileApi.FileViewModel
import com.example.filesapp.databinding.FragmentUploadFileSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File

class UploadFileSheet : BottomSheetDialogFragment() {

    private val fileApiView: FileViewModel by viewModels()
    private lateinit var binding: FragmentUploadFileSheetBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Upload file button
        binding.btnUploadFile.setOnClickListener {
            attachAndUploadAnyFile()
        }

        // Upload Image button
        binding.btnUploadImage.setOnClickListener {
            // TODO upload image on server (!)
        }

        // Upload Audio button
        binding.btnUploadAudio.setOnClickListener {
            // TODO upload audio on server (!)
        }
    }

    private fun attachAndUploadAnyFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a file")
                ,100)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Deprecated ("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val uri: Uri? = data.data
            val path: String = uri?.path.toString()
            val file = File(path)
            // fileApiView.uploadFile(file)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadFileSheetBinding.inflate(inflater, container,false)
        return binding.root
    }
}