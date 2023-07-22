package com.alextos.contactsapp.contacts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alextos.contactsapp.contacts.domain.Contact
import com.alextos.contactsapp.contacts.presentation.contact_list.ContactListEvent
import com.alextos.contactsapp.core.presentation.BottomSheetFromWish

@Composable
fun ContactDetailSheet(
    isOpened: Boolean,
    contact: Contact?,
    onEvent: (ContactListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheetFromWish(
        visible = isOpened,
        modifier = modifier.fillMaxWidth()
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

                PhotoSection(contact, onEvent)

                Text(
                    text = "${contact?.firstName} ${contact?.lastName}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )

                ButtonsSection(
                    onDeleteTapped = { onEvent(ContactListEvent.DeleteContact) },
                    onEditTapped = { contact?.let { onEvent(ContactListEvent.EditContact(it)) } }
                )

                ContactInfoSection(
                    title = "Phone number",
                    value = contact?.phoneNumber ?: "",
                    icon = Icons.Rounded.Phone,
                    modifier = Modifier.fillMaxWidth()
                )

                ContactInfoSection(
                    title = "Email",
                    value = contact?.email ?: "",
                    icon = Icons.Rounded.Email,
                    modifier = Modifier.fillMaxWidth()
                )
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
private fun PhotoSection(contact: Contact?, onEvent: (ContactListEvent) -> Unit) {
    if (contact?.photoBytes == null) {
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
                modifier = Modifier.size(50.dp)
            )
        }
    } else {
        ContactPhoto(
            contact = contact,
            modifier = Modifier
                .size(150.dp)
                .clickable {
                    onEvent(ContactListEvent.OnAddPhotoTapped)
                }
        )
    }
}

@Composable
fun ButtonsSection(
    onEditTapped: () -> Unit,
    onDeleteTapped: () -> Unit
) {
    Row(modifier = Modifier) {
        FilledTonalButton(
            imageVector = Icons.Rounded.Edit,
            imageDescription = "Edit contact",
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = onEditTapped
        )
        FilledTonalButton(
            imageVector = Icons.Rounded.Delete,
            imageDescription = "Delete contact",
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            onClick = onDeleteTapped
        )
    }
}

@Composable
private fun FilledTonalButton(
    imageVector: ImageVector,
    imageDescription: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    FilledTonalIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = imageDescription
        )
    }
}

@Composable
private fun ContactInfoSection(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(8.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = value,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}