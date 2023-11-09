package com.example.ownchatapp.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ownchatapp.R

@Composable
fun CraftedWithLove() {

    val context = LocalContext.current
    val heartIconId = "heart_icon"

    val style = SpanStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Black,
    )
    val inlineContent = mapOf(
        Pair(
            heartIconId,
            InlineTextContent(
                Placeholder(
                    width = 22.sp,
                    height = 18.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Bottom
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.red_heart),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxSize()
                )
            }
        )
    )
    Text(
        text = buildAnnotatedString {
            withStyle(style = style) {
                append(context.getString(R.string.developed_with))
            }
            appendInlineContent(heartIconId, "[icon]")
        },
        inlineContent = inlineContent
    )
    Text(
        text = stringResource(id = R.string.copyright_reserved),
        style = TextStyle(fontSize = 10.sp)
    )
}