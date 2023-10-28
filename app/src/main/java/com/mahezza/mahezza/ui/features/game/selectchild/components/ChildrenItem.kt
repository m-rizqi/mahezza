package com.mahezza.mahezza.ui.features.game.selectchild.components

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.features.home.components.BadgeChip
import com.mahezza.mahezza.ui.theme.AccentGreen
import com.mahezza.mahezza.ui.theme.AccentOrange
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsMedium10
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.White

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun ChildrenItem(
    modifier: Modifier = Modifier,
    imageUrl : String,
    name : String,
    age : Double,
    lastActivity : String,
    numberOfPlay : Int,
    numberOfCompletedChallenges : Int,
    timeOfPlay : Int,
    onClickListener : () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        onClick = onClickListener
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                GlideImage(
                    modifier = Modifier
                        .width(80.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp)),
                    model = imageUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    requestBuilderTransform = { request ->
                        val requestOptions = RequestOptions()
                            .placeholder(R.drawable.ic_loading_placeholder)
                            .error(R.drawable.ic_error_placeholder)
                        request.apply(requestOptions)
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = name,
                        style = PoppinsMedium16,
                        color = Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column() {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(10.dp),
                                    tint = GreyText,
                                    painter = painterResource(id = R.drawable.ic_user),
                                    contentDescription = stringResource(id = R.string.age)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(id = R.string.age),
                                    style = PoppinsMedium10,
                                    color = GreyText
                                )
                            }
                            Text(
                                text = stringResource(id = R.string.age_year, age),
                                style = PoppinsRegular12,
                                color = Black
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(10.dp),
                                    tint = GreyText,
                                    painter = painterResource(id = R.drawable.ic_history),
                                    contentDescription = stringResource(id = R.string.last_activity)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(id = R.string.last_activity),
                                    style = PoppinsMedium10,
                                    color = GreyText
                                )
                            }
                            Text(
                                text = lastActivity,
                                style = PoppinsRegular12,
                                color = Black
                            )
                        }
                    }
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (numberOfPlay > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    BadgeChip(
                        text = "${numberOfPlay}x ${stringResource(id = R.string.play)}",
                        backgroundColor = Color(0xFF166435).copy(alpha = 0.2f),
                        textColor = AccentGreen,
                        imageVector = Icons.Default.Extension,
                    )
                }
                if (timeOfPlay > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    BadgeChip(
                        text = "$timeOfPlay ${stringResource(id = R.string.minute)}",
                        backgroundColor = Color(0xFFFFD043).copy(alpha = 0.2f),
                        textColor = AccentYellowDark,
                        imageVector = Icons.Default.Timer,
                    )
                }
                if (numberOfCompletedChallenges > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    BadgeChip(
                        text = "$numberOfCompletedChallenges ${stringResource(id = R.string.challenge)}",
                        backgroundColor = Color(0xFFF52814).copy(alpha = 0.2f),
                        textColor = AccentOrange,
                        imageVector = Icons.Default.CheckCircle,
                    )
                }
            }
        }
    }
}