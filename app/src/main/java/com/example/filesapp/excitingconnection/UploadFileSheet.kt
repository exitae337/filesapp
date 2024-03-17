package com.example.filesapp.excitingconnection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.filesapp.databinding.FragmentUploadFileSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UploadFileSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentUploadFileSheetBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        // Upload file button
        binding.btnUploadFile.setOnClickListener {
            // TODO upload any file
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadFileSheetBinding.inflate(inflater, container,false)
        return binding.root
    }
}