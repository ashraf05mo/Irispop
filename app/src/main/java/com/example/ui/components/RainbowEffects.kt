package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GrandHotelFontFamily
import com.example.ui.theme.IrisBorderRainbowBrush
import com.example.ui.theme.IrisCardBg
import com.example.ui.theme.IrisRainbowBrush

@Composable
fun RainbowText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: androidx.compose.ui.unit.TextUnit = 28.sp,
    fontFamily: FontFamily = GrandHotelFontFamily,
    brush: Brush = IrisRainbowBrush
) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            brush = brush,
            fontSize = fontSize,
            fontFamily = fontFamily
        )
    )
}

fun Modifier.rainbowCardBorder(
    auraEnabled: Boolean = true,
    shape: Shape = RoundedCornerShape(26.dp),
    borderWidth: Dp = 1.8.dp
): Modifier = this
    .then(
        if (auraEnabled) {
            Modifier.shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = Color(0xFFFFAFBD),
                spotColor = Color(0xFFA1C4FD)
            )
        } else Modifier
    )
    .border(
        width = borderWidth,
        brush = IrisBorderRainbowBrush,
        shape = shape
    )
    .clip(shape)
    .background(IrisCardBg)

@Composable
fun HeartBurstOverlay(
    visible: Boolean,
    onAnimationEnd: () -> Unit
) {
    if (!visible) return

    val scale = remember { Animatable(0.2f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(visible) {
        scale.snapTo(0.2f)
        alpha.snapTo(1f)
        scale.animateTo(
            targetValue = 1.35f,
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
        )
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 300)
        )
        onAnimationEnd()
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .scale(scale.value),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "Heart Pop",
            tint = Color(0xFFF43F5E).copy(alpha = alpha.value),
            modifier = Modifier.size(90.dp)
        )
    }
}
