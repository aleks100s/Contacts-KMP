package com.alextos.contactsapp.core.database

import com.alextos.contactsapp.ContactsDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(
            schema = ContactsDatabase.Schema,
            name = "contacts.db"
        )
    }
}