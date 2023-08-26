package com.mahezza.mahezza.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyBorder
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular14
import com.mahezza.mahezza.ui.theme.White

@Composable
fun FilledAccentYellowButton(
    modifier : Modifier = Modifier,
    text : String,
    cornerRadius: Dp = 8.dp,
    verticalPadding : Dp = 12.dp,
    onClick : () -> Unit
){
    ElevatedButton(
        modifier = modifier,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = AccentYellow,
            contentColor = Black
        ),
        shape = RoundedCornerShape(cornerRadius),
        contentPadding = PaddingValues(vertical = verticalPadding),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = PoppinsMedium16,
        )
    }
}

@Preview
@Composable
fun MahezzaAccentYellowButtonPreview() {
    FilledAccentYellowButton(text = "Button") {
        
    }
}

@Composable
fun OutlinedAccentYellowButton(
    modifier : Modifier = Modifier,
    text : String,
    cornerRadius: Dp = 8.dp,
    onClick : () -> Unit
){
    OutlinedButton(
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = White,
            contentColor = Black,
        ),
        border = BorderStroke(1.dp, AccentYellow),
        shape = RoundedCornerShape(cornerRadius),
        contentPadding = PaddingValues(vertical = 12.dp),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = PoppinsMedium16,
        )
    }
}

@Preview
@Composable
fun OutlinedAccentYellowButtonPreview() {
    OutlinedAccentYellowButton(text = "Button") {
        
    }
}

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = stringResource(id = R.string.google)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.google),
                style = PoppinsRegular14,
                color = Black
            )
        }
    }
}

@Preview
@Composable
fun GoogleButtonPreview() {
    GoogleButton {

    }
}

@Composable
fun FilledAccentYellowExtendedButton(
    modifier : Modifier = Modifier,
    text : String,
    painter : Painter,
    cornerRadius: Dp = 8.dp,
    onClick : () -> Unit
){
    ElevatedButton(
        modifier = modifier,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = AccentYellow,
            contentColor = Black
        ),
        shape = RoundedCornerShape(cornerRadius),
        contentPadding = PaddingValues(vertical = 12.dp),
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = text
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = PoppinsMedium16,
        )
    }
}

@Preview
@Composable
fun FilledAccentYellowExtendedButtonPreview() {
    FilledAccentYellowExtendedButton(
        text = "Scan QR Code",
        painter = painterResource(id = R.drawable.ic_qrcode)
    ){

    }
}

@Composable
fun AccentYellowTextButton(
    modifier : Modifier = Modifier,
    text : String,
    cornerRadius: Dp = 8.dp,
    onClick : () -> Unit
){
    TextButton(
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = AccentYellowDark
        ),
        shape = RoundedCornerShape(cornerRadius),
        contentPadding = PaddingValues(vertical = 12.dp),
        onClick = onClick
    ) {
        Text(
            text = text,
            style = PoppinsMedium16,
        )
    }
}

@Preview
@Composable
fun AccentYellowTextButtonPreview() {
    AccentYellowTextButton(text = "Tutup Halaman") {
        
    }
}
