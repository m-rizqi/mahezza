package com.mahezza.mahezza.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsMedium16

enum class LayoutState {
    Shimmer,
    Empty,
    Content
}

@Composable
fun ShimmerEmptyContentLayout(
    modifier : Modifier = Modifier,
    state : LayoutState = LayoutState.Shimmer,
    emptyMessage : StringResource?,
    shimmer: @Composable (Brush) -> Unit,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier){
        when(state){
            LayoutState.Empty -> EmptyLayout(
                modifier = Modifier.align(Alignment.Center),
                emptyMessage = emptyMessage
            )
            LayoutState.Shimmer -> {
                AnimatedShimmer {brush ->
                    shimmer(brush)
                }
            }
            LayoutState.Content -> content()
        }
    }
}

@Composable
fun EmptyLayout(
    modifier : Modifier = Modifier,
    emptyMessage: StringResource?
) {
    Box(
        modifier = modifier
    ){
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(136.dp),
                painter = painterResource(id = R.drawable.ic_error_placeholder),
                contentDescription = stringResource(id = R.string.empty_or_error_screen)
            )
            emptyMessage?.let { message ->
                Text(
                    text = message.asString(),
                    style = PoppinsMedium16,
                    color = Black
                )
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun EmptyLayoutPreview() {
    EmptyLayout(
        modifier = Modifier.fillMaxSize(),
        emptyMessage = StringResource.DynamicString("Sesuatu Bermasalah")
    )
}