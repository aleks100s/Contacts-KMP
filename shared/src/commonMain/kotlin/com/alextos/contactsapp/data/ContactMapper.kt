package com.alextos.contactsapp.data

import com.alextos.contactsapp.domain.Contact
import database.ContactEntity

fun List<ContactEntity>.toContacts(): List<Contact> {
    return map { entity ->
        Contact(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email,
            phoneNumber = entity.phoneNumber,
            photoBytes = null
        )
    }
}