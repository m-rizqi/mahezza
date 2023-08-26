package com.mahezza.mahezza.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyBorder
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsRegular10
import com.mahezza.mahezza.ui.theme.PoppinsRegular14
import com.mahezza.mahezza.ui.theme.PoppinsRegular16
import com.mahezza.mahezza.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteTextField(
    modifier : Modifier = Modifier,
    suggestions: List<String>,
    initialValue : String = "",
    placeholder : String = "",
    errorText : String? = null,
    keyboardOptions : KeyboardOptions = KeyboardOptions.Default,
    keyboardActions : KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var inputValue by rememberSaveable { mutableStateOf(initialValue) }
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                onValueChange(it)
                expanded = inputValue.isNotEmpty()
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    expanded = it.isFocused && inputValue.isNotEmpty()
                },
            shape = RoundedCornerShape(8.dp),
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
        )
        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            ElevatedCard(
                modifier = Modifier.wrapContentSize(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                        .wrapContentHeight()
                        .verticalScroll(scrollState)
                ) {
                    suggestions.filter { suggestion ->
                        suggestion.contains(inputValue, ignoreCase = true)
                    }.forEach { suggestion ->
                        Text(
                            text = suggestion,
                            style = PoppinsRegular14,
                            color = Black,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    inputValue = suggestion
                                    expanded = false
                                    onValueChange(suggestion)
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AutoCompleteTextFieldWithTitle(
    modifier : Modifier = Modifier,
    title: String,
    suggestions: List<String>,
    placeholder : String = "",
    errorText : String? = null,
    keyboardOptions : KeyboardOptions = KeyboardOptions.Default,
    keyboardActions : KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit
){
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = PoppinsMedium14,
            color = Black
        )
        AutoCompleteTextField(
            modifier = modifier,
            suggestions = suggestions,
            placeholder = placeholder,
            errorText = errorText,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            onValueChange = {
                onValueChange(it)
            }
        )
    }
}

@Preview
@Composable
fun AutoCompleteTextFieldWithTitlePreview() {
    AutoCompleteTextFieldWithTitle(
        title = "Auto Complete",
        suggestions = listOf()
    ){

    }
}