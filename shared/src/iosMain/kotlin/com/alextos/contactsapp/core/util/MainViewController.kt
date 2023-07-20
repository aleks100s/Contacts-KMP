package com.alextos.contactsapp.core.util

import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.alextos.contactsapp.App
import com.alextos.contactsapp.core.di.AppModule
import com.alextos.contactsapp.core.presentation.ImagePickerFactory
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController() = ComposeUIViewController {
    val isDarkMode = UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    App(
        darkTheme = isDarkMode,
        dynamicColor = false,
        appModule = AppModule(),
        imagePicker = ImagePickerFactory(LocalUIViewController.current).createPicker()
    )
}