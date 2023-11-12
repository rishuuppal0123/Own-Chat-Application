package com.example.ownchatapp.viewModel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ownchatapp.R
import com.example.ownchatapp.events.LoginEvents
import com.example.ownchatapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: ChatClient
) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<LoginEvents>()
    val loginEvent = _loginEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private fun isValidUserName(username: String): Boolean {
        return !username.contains(" ") && username.length >= Constants.MIN_USERNAME_LENGTH
    }

    private fun btnEnable(): Boolean {
        return _uiState.value.name.text.length >= Constants.MIN_USERNAME_LENGTH && isValidUserName(
            _uiState.value.userName.text
        )
    }

    fun editName(name: TextFieldValue) {
        val isValidName = name.text.length >= Constants.MIN_USERNAME_LENGTH
        _uiState.update {
            it.copy(
                name = name,
                btnEnable = btnEnable(),
                nameError = if (!isValidName) R.string.invalid_name else null
            )
        }
    }

    fun editUserName(userName: TextFieldValue) {
        _uiState.update {
            it.copy(
                userName = userName,
                btnEnable = btnEnable(),
                userNameError = if (!isValidUserName(userName.text)) R.string.invalid_name else null
            )
        }
    }

    fun loginUser(userName: String, token: String? = null) {
        val trimmedUserName = userName.trim()
        viewModelScope.launch {
            if (isValidUserName(userName) && token != null) {
                loginRegisteredUser(trimmedUserName, token)
            } else if (isValidUserName(userName) && token == null) {
                loginGuestUser(trimmedUserName)
            }
        }
    }

    private fun loginGuestUser(userName: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        client.connectGuestUser(userId = userName, username = userName).enqueue { result ->
            _uiState.update {
                it.copy(isLoading = false)
            }
            if (result.isSuccess) {
                viewModelScope.launch {
                    _loginEvent.emit(LoginEvents.Success)
                }
            } else {
                viewModelScope.launch {
                    _loginEvent.emit(
                        LoginEvents.ErrorLogin(
                            result.errorOrNull()?.message ?: "Unknown User"
                        )
                    )
                }
            }
        }
    }

    private fun loginRegisteredUser(userName: String, token: String) {
        val user = User(id = userName, name = userName)
        _uiState.update {
            it.copy(isLoading = true)
        }

        client.connectUser(user = user, token = token).enqueue { result ->
            _uiState.update {
                it.copy(isLoading = false)
            }
            if (result.isSuccess) {
                viewModelScope.launch {
                    _loginEvent.emit(LoginEvents.Success)
                }
            } else {
                viewModelScope.launch {
                    _loginEvent.emit(
                        LoginEvents.ErrorLogin(
                            result.errorOrNull()?.message ?: "Unknown User"
                        )
                    )
                }
            }
        }
    }

    data class LoginUiState(
        val isLoading: Boolean = false,
        val name: TextFieldValue = TextFieldValue(),
        val userName: TextFieldValue = TextFieldValue(),
        val userNameError: Int? = null,
        val nameError: Int? = null,
        val btnEnable: Boolean = false
    )
}