package com.mahezza.mahezza.ui.features.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.theme.AccentGreen
import com.mahezza.mahezza.ui.theme.AccentOrange
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.Grey
import com.mahezza.mahezza.ui.theme.PoppinsMedium10
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.White

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalLayoutApi::class)
@Composable
fun ChildSummaryItem(
    modifier: Modifier = Modifier,
    childImageUrl: String,
    childName: String,
    numberOfPlay : Int,
    timeOfPlay : Int,
    numberOfCompletedChallenges : Int,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val cardWidth = remember {
        screenWidth / 1.6
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        border = BorderStroke(1.dp, Grey),
        elevation = CardDefaults.elevatedCardElevation(1.dp),
        modifier = modifier
            .width(cardWidth.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp
                        )
                    ),
                contentScale = ContentScale.Crop,
                model = childImageUrl,
                contentDescription = childName,
                requestBuilderTransform = { request ->
                    val requestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_loading_placeholder)
                        .error(R.drawable.ic_error_placeholder)
                    request.apply(requestOptions)
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = childName,
                    style = PoppinsMedium14,
                    color = Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (numberOfPlay > 0) {
                        BadgeChip(
                            text = "${numberOfPlay}x ${stringResource(id = R.string.play)}",
                            backgroundColor = Color(0xFF166435).copy(alpha = 0.2f),
                            textColor = AccentGreen,
                            imageVector = Icons.Default.Extension,
                        )
                    }
                    if (timeOfPlay > 0) {
                        BadgeChip(
                            text = "$timeOfPlay ${stringResource(id = R.string.minute)}",
                            backgroundColor = Color(0xFFFFD043).copy(alpha = 0.2f),
                            textColor = AccentYellowDark,
                            imageVector = Icons.Default.Timer,
                        )
                    }
                    if (numberOfCompletedChallenges > 0) {
                        BadgeChip(
                            text = "$numberOfCompletedChallenges ${stringResource(id = R.string.challenge)}",
                            backgroundColor = Color(0xFFF52814).copy(alpha = 0.2f),
                            textColor = AccentOrange,
                            imageVector = Icons.Default.CheckCircle,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun BadgeChip(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    textColor: Color,
    imageVector: ImageVector,
) {
    Surface(
        modifier = modifier.then(Modifier.padding(4.dp)),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .height(12.dp)
                    .width(12.dp),
                imageVector = imageVector,
                contentDescription = text,
                tint = textColor
            )
            Text(
                text = text,
                modifier = Modifier.padding(8.dp),
                color = textColor,
                style = PoppinsMedium10,
            )
        }
    }
}