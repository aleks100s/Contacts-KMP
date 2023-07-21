package com.alextos.contactsapp.contacts.data

import com.alextos.contactsapp.ContactsDatabase
import com.alextos.contactsapp.contacts.domain.Contact
import com.alextos.contactsapp.contacts.domain.ContactsDataSource
import com.alextos.contactsapp.core.data.ImageStorage
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import database.ContactEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock

class SqlDelightContactsDataSource(
    private val database: ContactsDatabase,
    private val imageStorage: ImageStorage
): ContactsDataSource {
    override fun getContacts(): Flow<List<Contact>> {
        return database.contactsQueries
            .getContacts()
            .asFlow()
            .mapToList()
            .map { mapToContacts(it) }
    }

    override fun getRecentContacts(amount: Int): Flow<List<Contact>> {
        return database.contactsQueries
            .getRecentContacts(amount.toLong())
            .asFlow()
            .mapToList()
            .map { mapToContacts(it) }
    }

    override suspend fun insertContact(contact: Contact) {
        val imagePath = contact.photoBytes?.let {
            imageStorage.saveImage(it)
        }
        val timestamp = Clock.System.now().toEpochMilliseconds()
        database.contactsQueries
            .insertContactEntity(
                id = contact.id,
                firstName = contact.firstName,
                lastName = contact.lastName,
                email = contact.email,
                phoneNumber = contact.phoneNumber,
                createdAt = timestamp,
                imagePath = imagePath
            )
    }

    override suspend fun deleteContact(contact: Contact) {
        val entity = database.contactsQueries
            .getContactById(contact.id ?: 0)
            .executeAsOne()

        entity.imagePath?.let { imageStorage.deleteImage(it) }

        database.contactsQueries
            .deleteContact(contact.id ?: 0)
    }

    private suspend fun mapToContacts(entities: List<ContactEntity>): List<Contact> {
        return supervisorScope {
            entities.map { entity ->
                async {
                    val bytes = entity.imagePath?.let { imageStorage.getImage(it) }
                    entity.toContact(photoBytes = bytes)
                }
            }
                .map { it.await() }
        }
    }
}