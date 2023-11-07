package com.example.ownchatapp.events

sealed interface ChannelEvents {
    data class Error(val error: String): ChannelEvents
    object Success: ChannelEvents
}