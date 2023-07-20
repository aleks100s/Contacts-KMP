package com.alextos.contactsapp.core.presentation

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable

actual class ImagePicker(
    private val activity: ComponentActivity
) {
    private lateinit var getContent: ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    actual fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
        getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                activity.contentResolver.openInputStream(uri)?.use {
                    onImagePicked(it.readAllBytes())
                }
            }
        }
    }

    actual fun pickImage() {
        getContent.launch("image/*")
    }
}