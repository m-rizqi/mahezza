package com.mahezza.mahezza.ui.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.mahezza.mahezza.R

@Composable
fun GifImage(
    modifier : Modifier = Modifier,
    gifUrl : String?,
    contentDescription : String?
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            }else{
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        modifier = modifier,
        contentScale = ContentScale.Crop,
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(gifUrl)
                .placeholder(R.drawable.ic_loading_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .build(),
            imageLoader = imageLoader,
        ),
        contentDescription = contentDescription
    )
}

@Preview
@Composable
fun GifImagePreview() {
    GifImage(
        gifUrl = "https://firebasestorage.googleapis.com/v0/b/mahezza-c3ed3.appspot.com/o/puzzles%2Fa80d4898-4278-11ee-be56-0242ac120002%2Fgradient_mesh_anim.gif?alt=media&token=d66ce88a-290c-4401-a562-7bd95e9a7a7d",
        contentDescription = null
    )
}