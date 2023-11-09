package com.example.ownchatapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ownchatapp.R
import com.example.ownchatapp.composable.LoginScreen
import com.example.ownchatapp.events.LoginEvents
import com.example.ownchatapp.ui.theme.OwnChatAppTheme
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

