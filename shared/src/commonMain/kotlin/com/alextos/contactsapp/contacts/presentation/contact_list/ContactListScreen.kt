package com.alextos.contactsapp.contacts.presentation.contact_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alextos.contactsapp.contacts.domain.Contact
import com.alextos.contactsapp.contacts.presentation.components.AddContactSheet
import com.alextos.contactsapp.contacts.presentation.components.ContactDetailSheet
import com.alextos.contactsapp.contacts.presentation.components.ContactListItem
import com.alextos.contactsapp.core.presentation.ImagePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    state: ContactListState,
    newContact: Contact?,
    onEvent: (ContactListEvent) -> Unit,
    imagePicker: ImagePicker
) {
    imagePicker.registerPicker { imageBytes ->
        onEvent(ContactListEvent.OnPhotoPicked(imageBytes))
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(ContactListEvent.OnAddNewContactTapped)
                },
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PersonAdd,
                    contentDescription = "Add contact"
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(it),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                ContactListHeader(state.contacts)
            }

            items(state.contacts) { contact ->
                ContactListItem(
                    contact = contact,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(ContactListEvent.SelectContact(contact))
                        }
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }

    AddContactSheet(
        state = state,
        newContact = newContact,
        isOpened = state.isAddContactSheetOpened,
        onEvent = { event ->
            if (event is ContactListEvent.OnAddPhotoTapped) {
                imagePicker.pickImage()
            }
            onEvent(event)
        }
    )

    ContactDetailSheet(
        isOpened = state.isSelectedContactSheetOpened,
        contact = state.selectedContact,
        onEvent = onEvent
    )
}

@Composable
private fun ContactListHeader(contacts: List<Contact>) {
    Text(
        text = "My contacts (${contacts.size})",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        fontWeight = FontWeight.Bold
    )
}