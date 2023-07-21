package com.alextos.contactsapp.core.di

import com.alextos.contactsapp.ContactsDatabase
import com.alextos.contactsapp.core.database.DatabaseDriverFactory
import com.alextos.contactsapp.contacts.data.SqlDelightContactsDataSource
import com.alextos.contactsapp.contacts.domain.ContactsDataSource
import com.alextos.contactsapp.core.data.ImageStorage

actual class AppModule {
    actual val contactsDataSource: ContactsDataSource by lazy {
        SqlDelightContactsDataSource(
            database = ContactsDatabase(
                driver = DatabaseDriverFactory().create()
            ),
            imageStorage = ImageStorage()
        )
    }
}