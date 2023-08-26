package com.mahezza.mahezza.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.White

@Composable
fun EmptyOrErrorScreen(
    message : String = ""
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ){
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(136.dp),
                painter = painterResource(id = R.drawable.ic_error_placeholder),
                contentDescription = stringResource(id = R.string.empty_or_error_screen)
            )
            if (message.isNotBlank()){
                Text(
                    text = message,
                    style = PoppinsMedium16,
                    color = Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun EmptyOrErrorScreenPreview() {
    EmptyOrErrorScreen("Pesan error")
}