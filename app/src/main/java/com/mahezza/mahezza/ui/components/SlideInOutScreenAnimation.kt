package com.mahezza.mahezza.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun SlideInOutScreenAnimation(
    visible: Boolean = true,
    content: @Composable () -> Unit
) {
    val offset = animateDpAsState(
        targetValue = if (visible) 0.dp else (-1000).dp, // Adjust the offset value as needed
        animationSpec = tween(durationMillis = 300)
    ).value

    // Apply the offset and opacity to the content
    content.invoke()
}