package com.alextos.contactsapp.contacts.data

import com.alextos.contactsapp.contacts.domain.Contact
import database.ContactEntity

fun ContactEntity.toContact(photoBytes: ByteArray?): Contact {
    return Contact(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        photoBytes = photoBytes
    )
}