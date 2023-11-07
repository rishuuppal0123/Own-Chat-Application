package com.example.ownchatapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ownchatapp.R
import com.example.ownchatapp.events.ChannelEvents
import com.example.ownchatapp.viewModel.ChannelListViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.channels.list.ChannelList
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChannelListActivity : ComponentActivity() {

    val viewModel: ChannelListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeToEvents()

        setContent {
            ChatTheme {

                var showDialog: Boolean by remember {
                    mutableStateOf(false)
                }
                var showLogoutDialog: Boolean by remember {
                    mutableStateOf(false)
                }

                if (showDialog) {
                    CreateChannelDialog(
                        dismissDialog = { channelName ->
                            viewModel.createChannel(channelName)
                            showDialog = false
                        }
                    )
                }
                if (showLogoutDialog) {
                    LogoutDialog(onDismiss = {
                        showLogoutDialog = false
                    }, onConfirm = {
                        viewModel.logout()
                        finish()
                        startActivity(Intent(this, LoginActivity::class.java))
                    })
                }
                ChannelsScreen(
                    title = "Your Chats",
                    isShowingSearch = true,
                    onItemClick = {
                        startActivity(ChatActivity.getIntent(this, channelId = it.cid))
                    },
                    onBackPressed = {
                        finish()
                    },
                    onHeaderActionClick = {
                        showDialog = true
                    },
                    onHeaderAvatarClick = {
                        showLogoutDialog = true
                    })
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateChannelDialog(
        dismissDialog: (String) -> Unit
    ) {
        var channelName by remember {
            mutableStateOf("")
        }
        AlertDialog(
            onDismissRequest = {
                dismissDialog(channelName)
            },
            title = {
                Text(text = stringResource(id = R.string.enter_channel_name))
            },
            text = {
                TextField(
                    value = channelName,
                    onValueChange = {
                        channelName = it
                    })
            },
            confirmButton = {
                Button(
                    onClick = {
                        dismissDialog(channelName)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = channelName.isNotEmpty()
                ) {
                    Text(text = stringResource(id = R.string.create_channel))
                }
            },
            dismissButton = {

            }
        )
    }

    private fun subscribeToEvents() {
        val cScope = lifecycleScope
        cScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.channelEvents.collect { event ->
                    when (event) {
                        is ChannelEvents.Error -> {
                            val errorMsg = event.error
                            showToast(errorMsg)
                        }

                        ChannelEvents.Success -> {
                            showToast(getString(R.string.channel_created_successfully))
                        }
                    }
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    @Composable
    private fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = null,
                    modifier = Modifier.size(52.dp)
                )
                Text(
                    text = stringResource(id = R.string.are_you_sure_to_logout),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.SansSerif
                    )
                )
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(4.dp),
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue )
                    ) {
                        Text("No")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(4.dp),
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue )
                    ) {
                        Text("Yes")
                    }
                }
            }
        }
    }

}
