package com.alextos.contactsapp.contacts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.alextos.contactsapp.contacts.domain.Contact
import com.alextos.contactsapp.contacts.presentation.contact_list.ContactListEvent
import com.alextos.contactsapp.contacts.presentation.contact_list.ContactListState
import com.alextos.contactsapp.core.presentation.BottomSheetFromWish

@Composable
fun AddContactSheet(
    state: ContactListState,
    newContact: Contact?,
    isOpened: Boolean,
    onEvent: (ContactListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheetFromWish(
        visible = isOpened,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(32.dp))

                PhotoSection(newContact, onEvent)

                FieldsSection(newContact, state, onEvent)

                Button(onClick = { onEvent(ContactListEvent.SaveContact) }) {
                    Text("Save")
                }
            }

            IconButton(
                onClick = {
                    onEvent(ContactListEvent.DismissContact)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close"
                )
            }
        }
    }
}

@Composable
private fun PhotoSection(newContact: Contact?, onEvent: (ContactListEvent) -> Unit) {
    if (newContact?.photoBytes == null) {
        Box(
            modifier = Modifier.size(150.dp)
                .clip(RoundedCornerShape(percent = 40))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                    onEvent(ContactListEvent.OnAddPhotoTapped)
                }
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(40)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add photo",
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(40.dp)
            )
        }
    } else {
        ContactPhoto(
            contact = newContact,
            modifier = Modifier
                .size(150.dp)
                .clickable {
                    onEvent(ContactListEvent.OnAddPhotoTapped)
                }
        )
    }
}

@Composable
private fun FieldsSection(
    newContact: Contact?,
    state: ContactListState,
    onEvent: (ContactListEvent) -> Unit
) {
    ContactTextField(
        value = newContact?.firstName ?: "",
        placeholder = "First name",
        error = state.firstNameError,
        onValueChanged = { value ->
            onEvent(ContactListEvent.OnFirstNameChanged(value))
        },
        modifier = Modifier.fillMaxWidth()
    )

    ContactTextField(
        value = newContact?.lastName ?: "",
        placeholder = "Last name",
        error = state.lastNameError,
        onValueChanged = { value ->
            onEvent(ContactListEvent.OnLastNameChanged(value))
        },
        modifier = Modifier.fillMaxWidth()
    )

    ContactTextField(
        value = newContact?.email ?: "",
        placeholder = "Email",
        error = state.emailError,
        onValueChanged = { value ->
            onEvent(ContactListEvent.OnEmailChanged(value))
        },
        modifier = Modifier.fillMaxWidth()
    )

    ContactTextField(
        value = newContact?.phoneNumber ?: "",
        placeholder = "Phone number",
        error = state.phoneNumberError,
        onValueChanged = { value ->
            onEvent(ContactListEvent.OnPhoneNumberChanged(value))
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactTextField(
    value: String,
    placeholder: String,
    error: String?,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        OutlinedTextField(
            value = value,
            placeholder = {
                Text(text = placeholder)
            },
            onValueChange = onValueChanged,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }
    }
}