package com.alextos.contactsapp.presentation.contact_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.alextos.contactsapp.domain.Contact
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactListViewModel: ViewModel() {
    private val _state = MutableStateFlow(ContactListState(contacts))
    val state = _state.asStateFlow()

    var newContact: Contact? by mutableStateOf(null)
        private set

    fun onEvent(event: ContactListEvent) {

    }
}

private val contacts = (1..50).map {
    Contact(
        id =  it.toLong(),
        firstName = "First $it",
        lastName = "Last $it",
        phoneNumber = "1234567890",
        email = "qwerty$it@gmail.com",
        photoBytes = null
    )
}