package com.alextos.contactsapp.contacts.data

import com.alextos.contactsapp.ContactsDatabase
import com.alextos.contactsapp.contacts.domain.Contact
import com.alextos.contactsapp.contacts.domain.ContactsDataSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class SqlDelightContactsDataSource(
    private val database: ContactsDatabase
): ContactsDataSource {
    override fun getContacts(): Flow<List<Contact>> {
        return database.contactsQueries
            .getContacts()
            .asFlow()
            .mapToList()
            .map { it.toContacts() }
    }

    override fun getRecentContacts(amount: Int): Flow<List<Contact>> {
        return database.contactsQueries
            .getRecentContacts(amount.toLong())
            .asFlow()
            .mapToList()
            .map { it.toContacts() }
    }

    override suspend fun insertContact(contact: Contact) {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        database.contactsQueries
            .insertContactEntity(
                id = contact.id,
                firstName = contact.firstName,
                lastName = contact.lastName,
                email = contact.email,
                phoneNumber = contact.phoneNumber,
                createdAt = timestamp,
                imagePath = null
            )
    }

    override suspend fun deleteContact(contact: Contact) {
        database.contactsQueries
            .deleteContact(contact.id ?: 0)
    }
}