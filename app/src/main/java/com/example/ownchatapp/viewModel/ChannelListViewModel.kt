package com.example.ownchatapp.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ownchatapp.events.ChannelEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.models.Filters
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
            val filter = Filters.`in`("members", "thierry")
            val offset = 0
            val limit = 10
            val request = QueryChannelsRequest(filter, offset, limit)
            chatClient.queryChannels(request).enqueue { result ->
                if (result.isSuccess) {
                    val channels = result.getOrNull()

                } else {
                    // Handle result.error()
                }
            }
            chatClient.createChannel(
                memberIds = listOf("thierry", "tomasso"),
                channelType = channelType,
                channelId = channelId,
                extraData = mapOf(
                    "name" to trimmedChannelName,
                    "image" to "https://bit.ly/2TIt8NR",
                    "source_detail" to mapOf("user_id" to 123),
                    "channel_detail" to mapOf(
                        "topic" to "Plants and Animals",
                        "rating" to "pg"
                    )
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