package com.alextos.contactsapp.core.database

import android.content.Context
import com.alextos.contactsapp.ContactsDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(
    private val context: Context
) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            schema = ContactsDatabase.Schema,
            context = context,
            name = "contacts.db"
        )
    }
}