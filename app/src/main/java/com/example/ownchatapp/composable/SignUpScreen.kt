package com.example.ownchatapp.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ownchatapp.ImageView
import com.example.ownchatapp.R
import com.example.ownchatapp.ui.theme.defaultBlue
import com.example.ownchatapp.viewModel.LoginViewModel
import io.getstream.chat.android.client.ChatClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen() {

    val context = LocalContext.current
    val loginViewModel = viewModel<LoginViewModel>()
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val chatClient = ChatClient.instance()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(contentAlignment = Alignment.Center) {
        ImageView(
            model = R.drawable.login_bg,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.6f,
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    focusManager.clearFocus()
                }
                .background(color = Color.Transparent)
                .padding(start = 36.dp, end = 36.dp, top = 72.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.chat_logo_new),
                contentDescription = "logo",
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
            )
            Text(
                text = stringResource(id = R.string.text_welcome),
                style = TextStyle(
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
            )
            OutlinedTextField(
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Gray.copy(alpha = 0.4f),
                    unfocusedLabelColor = Color.Black,
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = defaultBlue,
                    focusedLabelColor = Color.White,
                    textColor = Color.White
                ),
                maxLines = 1,
                value = uiState.userName,
                onValueChange = {
                    loginViewModel.editUserName(userName = it)
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
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.LightGray.copy(alpha = 0.8f),
                    disabledContentColor = Color.DarkGray.copy(alpha = 0.8f),
                    containerColor = defaultBlue.copy(alpha = 0.8f),
                ),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    loginViewModel.loginUser(
                        userName = uiState.userName.text,
                        token = context.getString(R.string.jwt_token)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 12.dp),
                enabled = uiState.btnEnable
            ) {
                Text(text = stringResource(id = R.string.text_login_as_user))
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(12.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            CraftedWithLove()
        }
    }
}