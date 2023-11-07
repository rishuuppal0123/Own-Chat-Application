package com.example.ownchatapp.events

sealed interface LoginEvents {
    data class  ErrorLogin(val error: String): LoginEvents
    object Success: LoginEvents
}