package com.example.ownchatapp

import android.util.Base64
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@Composable
fun ImageView(
    modifier: Modifier = Modifier,
    model: Any,
    alpha: Float = 1F,
    contentScale: ContentScale = ContentScale.Fit,
    brush: Brush? = null
) {
    val context = LocalContext.current
    val data = remember(model) {
        try {
            Base64.decode(model.toString(), Base64.DEFAULT)
        } catch (e: Exception) {
            model
        }
    }
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
//        AsyncImage(
//            modifier = Modifier.fillMaxSize(),
//            alpha = alpha,
//            contentScale = contentScale,
//            model = ImageRequest.Builder(context).data(data).decoderFactory(SvgDecoder.Factory())
//                .crossfade(true).also {
//                    if (token != null) {
//                        it.addHeader("Authorization", token!!)
//                    }
//                }.build(),
//            contentDescription = null,
//            onState = { state ->
//                showPlaceHolder = state !is AsyncImagePainter.State.Success
//            })
//        if (brush != null && !showPlaceHolder) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(brush)
//            )
//        }
    }
}
