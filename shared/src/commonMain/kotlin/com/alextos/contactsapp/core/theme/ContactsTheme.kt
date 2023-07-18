package com.alextos.contactsapp.core.theme

import androidx.compose.runtime.Composable

expect fun ContactsTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
)