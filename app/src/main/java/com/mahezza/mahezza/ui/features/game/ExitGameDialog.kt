package com.mahezza.mahezza.ui.features.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.components.BaseDialog
import com.mahezza.mahezza.ui.components.FilledDangerButton
import com.mahezza.mahezza.ui.components.OutlinedDangerButton
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsRegular14
import com.mahezza.mahezza.ui.theme.PoppinsRegular16
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold14
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold18
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold20
import com.mahezza.mahezza.ui.theme.RedDanger

@Composable
fun ExitGameDialog(
    onExit : () -> Unit,
    onDismissRequest: (Boolean) -> Unit
) {
    BaseDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .align(CenterHorizontally),
                text = stringResource(id = R.string.exit_game),
                style = PoppinsSemiBold18,
                color = RedDanger
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.game_will_be_saved_and_can_be_continue),
                style = PoppinsRegular14,
                color = Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledDangerButton(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(id = R.string.yes_exit)
                ) {
                    onExit()
                }
                OutlinedDangerButton(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(id = R.string.no_stay)
                ) {
                    onDismissRequest(false)
                }
            }
        }
    }
}

@Preview
@Composable
fun ExitGameDialogPreview() {
    ExitGameDialog(
        onExit = {},
        onDismissRequest = {}
    )
}