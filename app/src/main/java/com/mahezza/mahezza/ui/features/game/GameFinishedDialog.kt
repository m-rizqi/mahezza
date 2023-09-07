package com.mahezza.mahezza.ui.features.game

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.ui.components.BaseDialog
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.StackedPhotoProfiles
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyBorder
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular10
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold20

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GameFinishedDialog(
    onDismissRequest : (Boolean) -> Unit,
    elapsedTimeInMinutes : Float,
    subCourseCount : Int,
    challengeCompleted : Int,
    twibbonUrl : String,
    children : List<Child>,
    bannerUrl : String,
    onShare : () -> Unit
) {
    BaseDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.congratulation),
                style = PoppinsMedium16,
                color = Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.game_finished),
                style = PoppinsSemiBold20,
                color = Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy((-40).dp)
            ) {
                val circleImageModifier = remember {
                    Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = AccentYellow,
                            shape = CircleShape
                        )
                }
                GlideImage(
                    modifier = circleImageModifier,
                    model = twibbonUrl,
                    contentDescription = stringResource(id = R.string.game_finished),
                    contentScale = ContentScale.Crop,
                    requestBuilderTransform = { request ->
                        val requestOptions = RequestOptions()
                            .placeholder(R.drawable.ic_loading_placeholder)
                            .error(R.drawable.ic_error_placeholder)
                        request.apply(requestOptions)
                    }
                )
                children.map { it.photoUrl }.take(3).forEach { photoUrl ->
                    GlideImage(
                        modifier = circleImageModifier,
                        model = photoUrl,
                        contentDescription = stringResource(id = R.string.game_finished),
                        contentScale = ContentScale.Crop,
                        requestBuilderTransform = { request ->
                            val requestOptions = RequestOptions()
                                .placeholder(R.drawable.ic_loading_placeholder)
                                .error(R.drawable.ic_error_placeholder)
                            request.apply(requestOptions)
                        }
                    )
                }
                GlideImage(
                    modifier = circleImageModifier,
                    model = bannerUrl,
                    contentDescription = stringResource(id = R.string.game_finished),
                    contentScale = ContentScale.Crop,
                    requestBuilderTransform = { request ->
                        val requestOptions = RequestOptions()
                            .placeholder(R.drawable.ic_loading_placeholder)
                            .error(R.drawable.ic_error_placeholder)
                        request.apply(requestOptions)
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                ResumeItem(
                    modifier = Modifier.weight(1f),
                    quantity = String.format("%.2f", elapsedTimeInMinutes),
                    unit = stringResource(id = R.string.minute),
                    description = stringResource(id = R.string.play)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(GreyBorder)
                )
                ResumeItem(
                    modifier = Modifier.weight(1f),
                    quantity = subCourseCount.toString(),
                    unit = stringResource(id = R.string.story),
                    description = stringResource(id = R.string.taught)
                )
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(GreyBorder)
                )
                ResumeItem(
                    modifier = Modifier.weight(1f),
                    quantity = challengeCompleted.toString(),
                    unit = stringResource(id = R.string.type),
                    description = stringResource(id = R.string.challenge)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            FilledAccentYellowButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.share)
            ) {
                onShare()
            }
        }
    }
}

@Composable
fun ResumeItem(
    modifier: Modifier = Modifier,
    quantity : String,
    unit : String,
    description : String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = quantity,
            style = PoppinsSemiBold20,
            color = Black,
            lineHeight = 0.sp
        )
        Spacer(modifier = Modifier.height(0.dp))
        Text(
            text = unit,
            style = PoppinsRegular12,
            color = GreyText,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = PoppinsRegular12,
            color = Black,
        )
    }
}

@Preview
@Composable
fun GameFinishedDialogPreview() {
    GameFinishedDialog(
        onDismissRequest = {},
        elapsedTimeInMinutes = 5.6f,
        subCourseCount = 3,
        challengeCompleted = 5,
        twibbonUrl = "https://firebasestorage.googleapis.com/v0/b/mahezza-c3ed3.appspot.com/o/users%2Fgames%2FMahezza%20Family-Aliya%20Agnesa?alt=media&token=736d1be1-8584-4858-a10c-320747ff521a",
        children = listOf(
            Child(
                parentId = "8XaGJ0QB9JMe9wD8Juh7RqZjSoX2",
                id = "0f98f1c7-cce9-4709-a0a4-71b9e4ca111b",
                name = "Aliya Agnesa",
                gender = "Perempuan",
                birthdate = "11 Aug 2017",
                photoUrl = "https://images.unsplash.com/photo-1554342321-0776d282ceac?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=387&q=80",
                lastActivity = "Foto Bersama"
            )
        ),
        bannerUrl = "https://firebasestorage.googleapis.com/v0/b/mahezza-c3ed3.appspot.com/o/puzzles%2Fa80d4898-4278-11ee-be56-0242ac120002%2FGroup%20100.png?alt=media&token=4e26ff2b-8edd-42f1-8bba-9426b2ccce71",
    ) {

    }
}