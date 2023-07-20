package com.alextos.contactsapp.core.di

import com.alextos.contactsapp.contacts.domain.ContactsDataSource

expect class AppModule {
    val contactsDataSource: ContactsDataSource
}