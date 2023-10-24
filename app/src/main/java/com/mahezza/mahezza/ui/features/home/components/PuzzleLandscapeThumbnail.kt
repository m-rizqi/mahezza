package com.mahezza.mahezza.ui.features.home.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.data.model.Puzzle

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PuzzleLandscapeThumbnail(
    modifier: Modifier = Modifier,
    puzzle: Puzzle
) {
    GlideImage(
        modifier = modifier
            .height(140.dp)
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(8.dp))
        ,
        contentScale = ContentScale.Crop,
        model = puzzle.banner,
        contentDescription = puzzle.name,
        requestBuilderTransform = { request ->
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_loading_placeholder)
                .error(R.drawable.ic_error_placeholder)
            request.apply(requestOptions)
        }
    )
}