package com.example.ownchatapp.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ownchatapp.events.ChannelEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChannelListViewModel @Inject constructor(
    private val chatClient: ChatClient
) : ViewModel() {

    private val _channelEvents = MutableSharedFlow<ChannelEvents>()
    val channelEvents = _channelEvents.asSharedFlow()

    fun createChannel(channelName: String, channelType: String = "messaging") {
        val trimmedChannelName = channelName.trim()
        val channelId = UUID.randomUUID().toString()

        viewModelScope.launch {
            if (trimmedChannelName.isEmpty()) {
                _channelEvents.emit(
                    ChannelEvents.Error("he channel name can't be empty.")
                )
                return@launch
            }
            chatClient.createChannel(
                memberIds = emptyList(),
                channelType = channelType,
                channelId = channelId,
                extraData = mapOf(
                    "name" to trimmedChannelName,
                    "image" to "https://bit.ly/2TIt8NR"
                )
            ).enqueue { result ->
                if (result.isSuccess) {
                    viewModelScope.launch {
                        _channelEvents.emit(ChannelEvents.Success)
                    }
                } else {
                    viewModelScope.launch {
                        _channelEvents.emit(
                            ChannelEvents.Error(
                                 result.errorOrNull()?.message ?: "Unknown Error"
                            )
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    fun logout() {
        chatClient.disconnect(true)
    }
}