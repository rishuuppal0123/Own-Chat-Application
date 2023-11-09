package com.example.ownchatapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun ImageView(
    modifier: Modifier = Modifier,
    model: Any,
    alpha: Float = 1F,
    contentScale: ContentScale = ContentScale.Fit,
    brush: Brush? = null
) {
    val context = LocalContext.current
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            alpha = alpha,
            contentScale = contentScale,
            model = ImageRequest.Builder(context).data(model).decoderFactory(SvgDecoder.Factory())
                .crossfade(true).build(),
            contentDescription = null
        )
    }
}
