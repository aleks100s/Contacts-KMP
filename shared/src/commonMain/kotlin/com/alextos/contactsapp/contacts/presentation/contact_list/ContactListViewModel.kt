package com.alextos.contactsapp.contacts.presentation.contact_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.alextos.contactsapp.contacts.domain.Contact
import com.alextos.contactsapp.contacts.domain.ContactValidator
import com.alextos.contactsapp.contacts.domain.ContactsDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactListViewModel(
    private val contactsDataSource: ContactsDataSource
): ViewModel() {
    private val _state = MutableStateFlow(ContactListState())
    val state = combine(
        _state,
        contactsDataSource.getContacts(),
        contactsDataSource.getRecentContacts(10)
    ) { state, contacts, recentContacts->
        state.copy(
            contacts = contacts,
            recentlyAddedContacts = recentContacts
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ContactListState())

    var newContact: Contact? by mutableStateOf(null)
        private set

    fun onEvent(event: ContactListEvent) {
        when(event) {
            ContactListEvent.DeleteContact -> deleteContact()
            ContactListEvent.DismissContact -> dismissContact()
            is ContactListEvent.EditContact -> editContact(event.contact)
            ContactListEvent.OnAddNewContactTapped -> addNewContact()
            is ContactListEvent.OnEmailChanged -> changeEmail(event.value)
            is ContactListEvent.OnFirstNameChanged -> changeFirstName(event.value)
            is ContactListEvent.OnLastNameChanged -> changeLastName(event.value)
            is ContactListEvent.OnPhoneNumberChanged -> changePhone(event.value)
            is ContactListEvent.OnPhotoPicked -> changePhoto(event.bytes)
            ContactListEvent.SaveContact -> saveContact()
            is ContactListEvent.SelectContact -> selectContact(event.contact)
            ContactListEvent.OnAddPhotoTapped -> {}
        }
    }

    private fun deleteContact() {
        viewModelScope.launch {
            _state.value.selectedContact?.let { contact ->
                _state.update { it.copy(isSelectedContactSheetOpened = false) }
                contactsDataSource.deleteContact(contact)
                delay(300L)
                _state.update { it.copy(selectedContact = null) }
            }
        }
    }

    private fun dismissContact() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSelectedContactSheetOpened = false,
                    isAddContactSheetOpened = false,
                    firstNameError = null,
                    lastNameError = null,
                    emailError = null,
                    phoneNumberError = null
                )
            }
            delay(300L)
            newContact = null
            _state.update { it.copy(selectedContact = null) }
        }
    }

    private fun editContact(contact: Contact) {
        _state.update {
            it.copy(
                selectedContact = contact,
                isSelectedContactSheetOpened = false,
                isAddContactSheetOpened = true,
            )
        }
        newContact = contact
    }

    private fun addNewContact() {
        _state.update { it.copy(isAddContactSheetOpened = true) }
        newContact = Contact(
            id = null,
            firstName = "",
            lastName = "",
            email = "",
            phoneNumber = "",
            photoBytes = null
        )
    }

    private fun changeEmail(email: String) {
        newContact = newContact?.copy(email = email)
    }

    private fun changeFirstName(firstname: String) {
        newContact = newContact?.copy(firstName = firstname)
    }

    private fun changeLastName(lastName: String) {
        newContact = newContact?.copy(lastName = lastName)
    }

    private fun changePhone(phone: String) {
        newContact = newContact?.copy(phoneNumber = phone)
    }

    private fun changePhoto(bytes: ByteArray) {
        newContact = newContact?.copy(photoBytes = bytes)
    }

    private fun saveContact() {
        newContact?.let { contact ->
            val result = ContactValidator.validateContact(contact)
            val errors = listOfNotNull(
                result.firstNameError,
                result.lastNameError,
                result.emailError,
                result.phoneNumberError
            )

            if (errors.isEmpty()) {
                _state.update {
                    it.copy(
                        isAddContactSheetOpened = false,
                        firstNameError = null,
                        lastNameError = null,
                        emailError = null,
                        phoneNumberError = null
                    )
                }
                viewModelScope.launch {
                    contactsDataSource.insertContact(contact)
                    delay(300L)
                    newContact = null
                }
            } else {
                _state.update {
                    it.copy(
                        firstNameError = result.firstNameError,
                        lastNameError = result.lastNameError,
                        emailError = result.emailError,
                        phoneNumberError = result.phoneNumberError
                    )
                }
            }
        }
    }

    private fun selectContact(contact: Contact) {
        _state.update {
            it.copy(
                selectedContact = contact,
                isSelectedContactSheetOpened = true
            )
        }
    }
}