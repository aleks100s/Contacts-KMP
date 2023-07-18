package com.alextos.contactsapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform