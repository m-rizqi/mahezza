package com.mahezza.mahezza.ui.features.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.components.BaseDialog
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.TextFieldWithTitle
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsMedium16

@Composable
fun ForgotPasswordDialog(
    onDismissRequest: (Boolean) -> Unit,
    onSendResetEmail: (String) -> Unit
) {
    BaseDialog(onDismissRequest = onDismissRequest) {
        var email by rememberSaveable {
            mutableStateOf("")
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    style = PoppinsMedium16,
                    color = Black
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithTitle(
                title = stringResource(id = R.string.email),
                value = email,
                placeholder = stringResource(id = R.string.enter_your_email),
                onValueChange = {
                    email = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilledAccentYellowButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.send_reset_email)
            ) {
                onSendResetEmail(email)
                onDismissRequest(false)
            }
        }
    }
}

@Preview
@Composable
fun ForgotPasswordDialogPreview() {
    ForgotPasswordDialog(onDismissRequest = {}, onSendResetEmail = {})
}