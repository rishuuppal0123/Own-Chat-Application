package com.example.ownchatapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dagger.hilt.android.qualifiers.ApplicationContext
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.ui.theme.StreamShapes
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

class ChatActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)

        if (channelId == null) {
            finish()
            return
        }

        setContent {
            ChatTheme(shapes = StreamShapes.defaultShapes().copy(
                avatar = RoundedCornerShape(8.dp),
                attachment = RoundedCornerShape(16.dp),
                myMessageBubble = RoundedCornerShape(16.dp),
                otherMessageBubble = RoundedCornerShape(16.dp),
                inputField = RectangleShape
            )) {
                MessagesScreen(
                    viewModelFactory = MessagesViewModelFactory(
                        context = applicationContext,
                        channelId = channelId
                    ),
                    onBackPressed = {
                        finish()
                    },
                )
            }
        }
    }

    companion object {
        private const val KEY_CHANNEL_ID = "channelId"

        fun getIntent(context: Context, channelId: String): Intent {
            return Intent(context, ChatActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}