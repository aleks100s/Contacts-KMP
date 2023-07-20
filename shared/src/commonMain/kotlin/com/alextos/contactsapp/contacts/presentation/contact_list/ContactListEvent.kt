package com.alextos.contactsapp.contacts.presentation.contact_list

import com.alextos.contactsapp.contacts.domain.Contact

sealed interface ContactListEvent {
    object OnAddNewContactTapped: ContactListEvent
    object DismissContact: ContactListEvent
    data class OnFirstNameChanged(val value: String): ContactListEvent
    data class OnLastNameChanged(val value: String): ContactListEvent
    data class OnEmailChanged(val value: String): ContactListEvent
    data class OnPhoneNumberChanged(val value: String): ContactListEvent
    class OnPhotoPicked(val bytes: ByteArray): ContactListEvent
    object OnAddPhotoTapped: ContactListEvent
    object SaveContact: ContactListEvent
    data class SelectContact(val contact: Contact): ContactListEvent
    data class EditContact(val contact: Contact): ContactListEvent
    object DeleteContact: ContactListEvent
}