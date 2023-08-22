package com.mahezza.mahezza.ui.features.register

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mahezza.mahezza.R
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.auth.RegisterWithEmailAndPasswordUseCase
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.GoogleButton
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.components.PasswordToggleTextFieldWithTitle
import com.mahezza.mahezza.ui.components.TextFieldWithTitle
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsBold32
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.PoppinsRegular14
import com.mahezza.mahezza.ui.theme.White
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel,
) {
    changeStatusBarColor(color = AccentYellow)
    val scrollState = rememberScrollState()
    val email = registerViewModel.email.collectAsState()
    val password = registerViewModel.password.collectAsState()
    val uiState = registerViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val registerWithGoogleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {result ->
            if (result.resultCode == RESULT_OK){
                registerViewModel.onEvent(RegisterEvent.OnGoogleSignInResult(result.data))
            }
        }
    )

    LaunchedEffect(key1 = uiState.value.shouldStartCreateProfileScreen){
        val startProfileScreen = uiState.value.shouldStartCreateProfileScreen
        if (startProfileScreen != null){
            showToast(context, context.getString(R.string.register_success))
            navController.navigate("${Routes.CreateProfile}/${startProfileScreen.userId}")
            registerViewModel.onEvent(RegisterEvent.OnCreateProfileScreenStarted)
        }
    }

    LaunchedEffect(key1 = uiState.value.generalError){
        if (uiState.value.generalError != null){
            showToast(context, uiState.value.generalError?.asString(context))
            registerViewModel.onEvent(RegisterEvent.OnGeneralMessageShowed)
        }
    }

    LaunchedEffect(key1 = uiState.value.signInResultResponse){
        uiState.value.signInResultResponse?.intentSender?.let { intentSender ->
           registerWithGoogleLauncher.launch(
               IntentSenderRequest.Builder(intentSender).build()
           )
            registerViewModel.onEvent(RegisterEvent.OnGoogleSignInStarted)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
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
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    style = PoppinsBold32,
                    color = Black
                )
                Text(
                    text = stringResource(id = R.string.start_improve_and_observe_mental_health),
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
                errorText = uiState.value.emailError?.asString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                value = email.value,
                onValueChange = registerViewModel::setEmail
            )
            Spacer(modifier = Modifier.height(24.dp))
            PasswordToggleTextFieldWithTitle(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.password),
                placeholder = stringResource(id = R.string.examplepassword),
                helperText = stringResource(id = R.string.min_8_chars_letter_and_number),
                errorText = uiState.value.passwordError?.asString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = {
                        registerViewModel.onEvent(RegisterEvent.OnRegisterClicked)
                    }
                ),
                value = password.value,
                onValueChange = registerViewModel::setPassword
            )
            Spacer(modifier = Modifier.height(24.dp))
            FilledAccentYellowButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.create_account),
                onClick = { registerViewModel.onEvent(RegisterEvent.OnRegisterClicked) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.or_register_with),
                style = PoppinsRegular14,
                color = Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            GoogleButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            ) {
                registerViewModel.onEvent(RegisterEvent.OnRegisterWithGoogleClicked)
            }
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(id = R.string.have_account),
                    style = PoppinsRegular12,
                    color = Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    },
                    text = stringResource(id = R.string.login),
                    style = PoppinsMedium14,
                    color = AccentYellowDark
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

    }
    if (uiState.value.isShowLoading){
        LoadingScreen()
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
//    val viewModel = RegisterViewModel(SavedStateHandle(), object : RegisterWithEmailAndPasswordUseCase{
//        override suspend fun invoke(email: String, password: String): Result<String> {
//            return Result.Success("")
//        }
//    })
//    RegisterScreen(navController = rememberNavController(), registerViewModel = viewModel)
}