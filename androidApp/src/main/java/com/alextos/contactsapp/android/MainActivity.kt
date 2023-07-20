package com.alextos.contactsapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import com.alextos.contactsapp.App
import com.alextos.contactsapp.core.di.AppModule
import com.alextos.contactsapp.core.presentation.ImagePickerFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = false,
                appModule = AppModule(context = LocalContext.current.applicationContext),
                imagePicker = ImagePickerFactory().createPicker()
            )
        }
    }
}
