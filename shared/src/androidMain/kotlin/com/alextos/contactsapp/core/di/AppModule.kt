package com.alextos.contactsapp.core.di

import android.content.Context
import com.alextos.contactsapp.ContactsDatabase
import com.alextos.contactsapp.core.database.DatabaseDriverFactory
import com.alextos.contactsapp.contacts.data.SqlDelightContactsDataSource
import com.alextos.contactsapp.contacts.domain.ContactsDataSource
import com.alextos.contactsapp.core.data.ImageStorage

actual class AppModule(
    private val context: Context
) {
    actual val contactsDataSource: ContactsDataSource by lazy {
        SqlDelightContactsDataSource(
            database = ContactsDatabase(
                driver = DatabaseDriverFactory(context).create()
            ),
            imageStorage = ImageStorage(context)
        )
    }
}