package com.mahezza.mahezza.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsMedium14

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StackedPhotoProfiles(
    modifier : Modifier = Modifier,
    photoProfiles : List<String>,
    maxPhotoShowed : Int = 3,
    imageSize : Dp = 32.dp,
    offset : Dp = (-12).dp
) {
    val circleImageModifier = remember {
        Modifier
            .size(imageSize)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = AccentYellow,
                shape = CircleShape
            )
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(offset)
    ){
        photoProfiles.take(3).forEach {photoUrl ->
            GlideImage(
                modifier = circleImageModifier,
                model = photoUrl,
                contentDescription = stringResource(id = R.string.saved_child_photo_profile),
                contentScale = ContentScale.Crop,
                requestBuilderTransform = { request ->
                    val requestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_loading_placeholder)
                        .error(R.drawable.ic_error_placeholder)
                    request.apply(requestOptions)
                }
            )
        }
        val offsetSize = remember {
            photoProfiles.size - maxPhotoShowed
        }
        if (offsetSize > 0){
            Box(
                modifier = circleImageModifier
                    .background(AccentYellow)
            ){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.plus_param, offsetSize),
                    style = PoppinsMedium14,
                    color = Black
                )
            }
        }
    }
}

@Preview
@Composable
fun StackedPhotoProfilesPreview() {
    StackedPhotoProfiles(
        photoProfiles = listOf(
            "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=2000",
            "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=2000",
            "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=2000",
            "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=2000",
            "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=2000",
        )
    )
}