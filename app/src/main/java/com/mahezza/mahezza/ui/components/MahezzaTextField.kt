package com.mahezza.mahezza.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyBorder
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsRegular10
import com.mahezza.mahezza.ui.theme.PoppinsRegular16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTextField(
    modifier : Modifier = Modifier,
    value : String = "",
    placeholder : String = "",
    errorText : String? = null,
    keyboardOptions : KeyboardOptions = KeyboardOptions.Default,
    keyboardActions : KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit
){
    OutlinedTextField(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        value = value,
        textStyle = PoppinsRegular16,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Black,
            unfocusedBorderColor = GreyBorder,
            focusedBorderColor = AccentYellow
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        placeholder = {
            Text(
                text = placeholder,
                style = PoppinsRegular16,
                color = GreyBorder
            )
        },
        isError = errorText != null,
        supportingText = {
            if (errorText != null){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText,
                    style = PoppinsRegular10,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        onValueChange = onValueChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithTitle(
    modifier : Modifier = Modifier,
    title : String,
    value : String = "",
    placeholder : String = "",
    errorText : String? = null,
    keyboardOptions : KeyboardOptions = KeyboardOptions.Default,
    keyboardActions : KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = PoppinsMedium14,
            color = Black
        )
        DefaultTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            placeholder = placeholder,
            errorText = errorText,
            onValueChange = onValueChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordToggleTextFieldWithTitle(
    modifier: Modifier = Modifier,
    title: String,
    value: String = "",
    placeholder: String = "",
    helperText: String = "",
    errorText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = PoppinsMedium14,
            color = Black
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            textStyle = PoppinsRegular16,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Black,
                unfocusedBorderColor = GreyBorder,
                focusedBorderColor = AccentYellow
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val visibilityIcon: ImageVector = if (passwordVisible) {
                    Icons.Default.Visibility
                } else {
                    Icons.Default.VisibilityOff
                }
                IconToggleButton(
                    checked = passwordVisible,
                    onCheckedChange = { passwordVisible = it }
                ) {
                    Icon(imageVector = visibilityIcon, contentDescription = stringResource(id = R.string.password_visibility))
                }
            },
            placeholder = {
                Text(
                    text = placeholder,
                    style = PoppinsRegular16,
                    color = GreyBorder
                )
            },
            supportingText = {
                Text(
                    text = errorText ?: helperText,
                    style = PoppinsRegular10,
                    color = if (errorText == null) GreyText else MaterialTheme.colorScheme.error
                )
            },
            isError = errorText != null,
            onValueChange = onValueChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldWithTitlePreview() {
    TextFieldWithTitle(
        title = "Title",
        placeholder = "Placeholder"
    ){

    }
}

@Preview(showBackground = true)
@Composable
fun PasswordToggleTextFieldPreview() {
    PasswordToggleTextFieldWithTitle(
        title = "Password",
        value = "examplePassword",
        onValueChange = {},
        placeholder = "Enter password",
        helperText = "This is helper text"
    )
}