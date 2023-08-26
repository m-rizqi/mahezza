package com.mahezza.mahezza.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.theme.White

@Composable
fun BaseDialog(
    onDismissRequest: (Boolean) -> Unit,
    content: @Composable() () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd),
                        onClick = { onDismissRequest(false) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.close)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box{
                    content()
                }
            }
        }
    }
}

@Preview
@Composable
fun BaseDialogPreview() {
    BaseDialog(onDismissRequest = {}) {
        
    }
}