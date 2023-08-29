package com.mahezza.mahezza.ui.features.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.GoogleButton
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.components.PasswordToggleTextFieldWithTitle
import com.mahezza.mahezza.ui.components.TextFieldWithTitle
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsBold32
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.PoppinsRegular14
import com.mahezza.mahezza.ui.theme.PoppinsRegular16
import com.mahezza.mahezza.ui.theme.White

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    changeStatusBarColor(color = AccentYellow)
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val email = loginViewModel.email.collectAsState()
    val password = loginViewModel.password.collectAsState()
    val uiState = loginViewModel.loginUiState.collectAsState()
    var isShowForgotPasswordDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val loginWithGoogleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {result ->
            if (result.resultCode == Activity.RESULT_OK){
                loginViewModel.onEvent(LoginEvent.OnGoogleSignInResult(result.data))
            }
        }
    )

    LaunchedEffect(key1 = uiState.value.generalError){
        uiState.value.generalError?.let { message ->
            showToast(context, message.asString(context))
            loginViewModel.onEvent(LoginEvent.OnGeneralErrorShowed)
        }
    }

    LaunchedEffect(key1 = uiState.value.shouldGoToDashboard){
        if (uiState.value.shouldGoToDashboard){
            navController.navigate(Routes.Dashboard){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
            loginViewModel.onEvent(LoginEvent.OnDashboardScreenStarted)
        }
    }

    LaunchedEffect(key1 = uiState.value.signInResultResponse){
        uiState.value.signInResultResponse?.intentSender?.let { intentSender ->
            loginWithGoogleLauncher.launch(
                IntentSenderRequest.Builder(intentSender).build()
            )
            loginViewModel.onEvent(LoginEvent.OnGoogleSignInStarted)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(AccentYellow),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
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
            Image(
                modifier = Modifier
                    .size(200.dp),
                painter = painterResource(id = R.drawable.login_illustration),
                contentDescription = stringResource(id = R.string.login_illustration)
            )
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
                        loginViewModel.onEvent(LoginEvent.OnLoginClicked)
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
                        isShowForgotPasswordDialog = true
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
                onClick = { loginViewModel.onEvent(LoginEvent.OnLoginClicked) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier
                    .align(CenterHorizontally),
                text = stringResource(id = R.string.or_login_with),
                style = PoppinsRegular14,
                color = Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            GoogleButton(
                modifier = Modifier
                    .align(CenterHorizontally),
            ) {
                loginViewModel.onEvent(LoginEvent.OnLoginWithGoogleClicked)
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(id = R.string.dont_have_an_account),
                    style = PoppinsRegular12,
                    color = Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.Register)
                    },
                    text = stringResource(id = R.string.register),
                    style = PoppinsMedium14,
                    color = AccentYellowDark
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

    }
    LoadingScreen(isShowLoading = uiState.value.isShowLoading)

    if (isShowForgotPasswordDialog){
        ForgotPasswordDialog(
            onDismissRequest = {
                isShowForgotPasswordDialog = it
            }, 
            onSendResetEmail = {
                loginViewModel.onEvent(LoginEvent.OnForgotPasswordRequest(it))
            }
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
//    val viewModel = LoginViewModel(SavedStateHandle())
//    LoginScreen(navController = rememberNavController(), loginViewModel = viewModel)
}