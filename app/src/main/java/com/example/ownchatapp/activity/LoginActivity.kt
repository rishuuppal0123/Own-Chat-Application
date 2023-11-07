package com.example.ownchatapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ownchatapp.R
import com.example.ownchatapp.events.LoginEvents
import com.example.ownchatapp.ui.theme.OwnChatAppTheme
import com.example.ownchatapp.ui.theme.lavender
import com.example.ownchatapp.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToEvents()
        setContent {
            OwnChatAppTheme {
                LoginScreen()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginScreen() {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//        Box(contentAlignment = Alignment.Center, ) {
//            Image(
//                painter = painterResource(id = R.drawable.login_bg),
//                contentDescription = "background",
//                modifier = Modifier.fillMaxSize().alpha(0.7f),
//                contentScale = ContentScale.FillBounds
//            )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = lavender)
                .padding(horizontal = 36.dp, vertical = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.chat_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
            )
            OutlinedTextField(
                value = uiState.name,
                onValueChange = {
                    viewModel.editName(userName = it)
                },
                label = {
                    Text(text = stringResource(id = R.string.text_enter_username))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Button(
                onClick = {
                    viewModel.loginUser(
                        userName = uiState.name.text,
                        token = getString(R.string.jwt_token)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 12.dp),
                enabled = uiState.btnEnable
            ) {
                Text(text = stringResource(id = R.string.text_login_as_user))
            }
            Button(
                onClick = {
                    viewModel.loginUser(uiState.name.text)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.btnEnable
            ) {
                Text(text = stringResource(id = R.string.text_login_as_guest))
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(12.dp))
            }
        }
//        }
    }

    private fun subscribeToEvents() {
        val cScope = lifecycleScope
        cScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.loginEvent.collect { event ->
                    when (event) {

                        is LoginEvents.ErrorLogin -> {
                            val errorMsg = "${getString(R.string.error)}: ${event.error}"
                            showToast(errorMsg)
                        }

                        LoginEvents.Success -> {
                            showToast(getString(R.string.login_successful))
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    ChannelListActivity::class.java
                                )
                            )
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

