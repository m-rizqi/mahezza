package com.mahezza.mahezza.ui.features.redeempuzzle

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.components.BaseDialog
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold20

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PuzzleRedeemedDialog(
    puzzleName : String,
    bannerUrl : String,
    onDismissRequest: (Boolean) -> Unit
) {
    BaseDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.puzzle_redeemed),
                style = PoppinsMedium16,
                color = Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = puzzleName,
                style = PoppinsSemiBold20,
                color = Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            GlideImage(
                model = bannerUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.7f)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillBounds,
                contentDescription = puzzleName,
                requestBuilderTransform = { request ->
                    val requestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_loading_placeholder)
                        .error(R.drawable.ic_error_placeholder)
                    request.apply(requestOptions)
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            FilledAccentYellowButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.close)
            ) {
                onDismissRequest(false)
            }
        }
    }
}

@Preview
@Composable
fun PuzzleRedeemedDialogPreview() {
    PuzzleRedeemedDialog(
        "Mahezza Family",
        "https://firebasestorage.googleapis.com/v0/b/mahezza-c3ed3.appspot.com/o/puzzles%2Fa80d4898-4278-11ee-be56-0242ac120002%2FGroup%20100.png?alt=media&token=4e26ff2b-8edd-42f1-8bba-9426b2ccce71"
    ) {}
}