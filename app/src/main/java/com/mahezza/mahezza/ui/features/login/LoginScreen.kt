package com.mahezza.mahezza.ui.features.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.PasswordToggleTextFieldWithTitle
import com.mahezza.mahezza.ui.components.TextFieldWithTitle
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsBold32
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular14
import com.mahezza.mahezza.ui.theme.PoppinsRegular16

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    changeStatusBarColor(color = AccentYellow)
    val scrollState = rememberScrollState()
    val email = loginViewModel.email.collectAsState()
    val password = loginViewModel.password.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(AccentYellow)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    style = PoppinsBold32,
                    color = Black
                )
                Text(
                    text = stringResource(id = R.string.continue_mental_health_development),
                    style = PoppinsMedium14,
                    color = Black
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp)
        ) {
            TextFieldWithTitle(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.email),
                placeholder = stringResource(id = R.string.youremailcom),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                value = email.value,
                onValueChange = loginViewModel::setEmail
            )
            Spacer(modifier = Modifier.height(24.dp))
            PasswordToggleTextFieldWithTitle(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.password),
                placeholder = stringResource(id = R.string.examplepassword),
                helperText = stringResource(id = R.string.min_8_chars_letter_and_number),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = {
                        // Login
                    }
                ),
                value = password.value,
                onValueChange = loginViewModel::setPassword
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        // Forgot Password
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.forgot_password),
                        style = PoppinsMedium14,
                        color = AccentYellowDark
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            FilledAccentYellowButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.login),
                onClick = {
                    // Login
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.or_login_with),
                style = PoppinsRegular14,
                color = Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedIconButton(
                onClick = {
                    // Login With Google
                }
            ) {
                
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    val viewModel = LoginViewModel(SavedStateHandle())
    LoginScreen(navController = rememberNavController(), loginViewModel = viewModel)
}