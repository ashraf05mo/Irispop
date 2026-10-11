package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GrandHotelFontFamily
import com.example.ui.theme.IrisBorderRainbowBrush
import com.example.ui.theme.IrisRainbowBrush
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SplashScreen(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(500))
    ) {
        var startAnim by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            startAnim = true
        }

        // Top shard translation (converges from -240dp down to 0)
        val topOffsetY by animateDpAsState(
            targetValue = if (startAnim) (-45).dp else (-260).dp,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            label = "topShard"
        )
        // Bottom shard translation (converges from +240dp up to 0)
        val bottomOffsetY by animateDpAsState(
            targetValue = if (startAnim) (45).dp else (260).dp,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            label = "bottomShard"
        )
        // Left shard translation (converges from -240dp to 0)
        val leftOffsetX by animateDpAsState(
            targetValue = if (startAnim) (-45).dp else (-260).dp,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            label = "leftShard"
        )
        // Right shard translation (converges from +240dp to 0)
        val rightOffsetX by animateDpAsState(
            targetValue = if (startAnim) (45).dp else (260).dp,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            label = "rightShard"
        )

        // Center emblem scale & alpha (bursts in once shards arrive)
        val centerScale by animateFloatAsState(
            targetValue = if (startAnim) 1f else 0.4f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "centerScale"
        )
        val centerAlpha by animateFloatAsState(
            targetValue = if (startAnim) 1f else 0f,
            animationSpec = tween(durationMillis = 800, delayMillis = 300),
            label = "centerAlpha"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D1017))
                .testTag("splash_screen"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                // Converging 4-part Shards & Center Emblem Box
                Box(
                    modifier = Modifier
                        .size(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Top shard
                    Box(
                        modifier = Modifier
                            .offset(y = topOffsetY)
                            .size(width = 80.dp, height = 36.dp)
                            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFFF9A9E), Color(0xFFFECFEF))
                                )
                            )
                            .blur(4.dp)
                            .alpha(0.85f)
                    )

                    // Bottom shard
                    Box(
                        modifier = Modifier
                            .offset(y = bottomOffsetY)
                            .size(width = 80.dp, height = 36.dp)
                            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF78FFD6), Color(0xFFA8FF78))
                                )
                            )
                            .blur(4.dp)
                            .alpha(0.85f)
                    )

                    // Left shard
                    Box(
                        modifier = Modifier
                            .offset(x = leftOffsetX)
                            .size(width = 36.dp, height = 80.dp)
                            .clip(RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFFF6584), Color(0xFFFFEAA7))
                                )
                            )
                            .blur(4.dp)
                            .alpha(0.85f)
                    )

                    // Right shard
                    Box(
                        modifier = Modifier
                            .offset(x = rightOffsetX)
                            .size(width = 36.dp, height = 80.dp)
                            .clip(RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFFA1C4FD), Color(0xFFE0C3FC))
                                )
                            )
                            .blur(4.dp)
                            .alpha(0.85f)
                    )

                    // Center Converging Irispop Emblem
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .scale(centerScale)
                            .alpha(centerAlpha)
                            .shadow(
                                elevation = 24.dp,
                                shape = RoundedCornerShape(38.dp),
                                ambientColor = Color(0xFFFBC2EB),
                                spotColor = Color(0xFFA6C1EE)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Squircle base
                            drawRoundRect(
                                color = Color(0xFF131722),
                                size = Size(w, h),
                                cornerRadius = CornerRadius(36.dp.toPx(), 36.dp.toPx())
                            )
                            // Outer Rainbow Border
                            drawRoundRect(
                                brush = IrisBorderRainbowBrush,
                                size = Size(w, h),
                                cornerRadius = CornerRadius(36.dp.toPx(), 36.dp.toPx()),
                                style = Stroke(width = 5.dp.toPx())
                            )

                            // Pop sensor dot
                            drawCircle(
                                brush = IrisRainbowBrush,
                                radius = 7.dp.toPx(),
                                center = Offset(w * 0.8f, h * 0.78f)
                            )

                            // Central Iris Ring
                            drawCircle(
                                brush = IrisBorderRainbowBrush,
                                radius = 32.dp.toPx(),
                                center = Offset(w * 0.5f, h * 0.5f),
                                style = Stroke(width = 2.5.dp.toPx())
                            )

                            // Social Communication Bubble
                            val bubblePath = Path().apply {
                                val cx = w * 0.5f
                                val cy = h * 0.5f
                                moveTo(cx - 20.dp.toPx(), cy - 10.dp.toPx())
                                cubicTo(
                                    cx - 24.dp.toPx(), cy - 24.dp.toPx(),
                                    cx + 24.dp.toPx(), cy - 24.dp.toPx(),
                                    cx + 20.dp.toPx(), cy - 10.dp.toPx()
                                )
                                cubicTo(
                                    cx + 26.dp.toPx(), cy + 8.dp.toPx(),
                                    cx + 10.dp.toPx(), cy + 20.dp.toPx(),
                                    cx - 4.dp.toPx(), cy + 22.dp.toPx()
                                )
                                lineTo(cx - 14.dp.toPx(), cy + 28.dp.toPx())
                                lineTo(cx - 10.dp.toPx(), cy + 18.dp.toPx())
                                close()
                            }
                            drawPath(
                                path = bubblePath,
                                brush = IrisRainbowBrush
                            )

                            // Inner connection silhouette dots
                            drawCircle(
                                color = Color(0xFF131722),
                                radius = 3.5.dp.toPx(),
                                center = Offset(w * 0.46f, h * 0.46f)
                            )
                            drawLine(
                                color = Color(0xFF131722),
                                start = Offset(w * 0.40f, h * 0.54f),
                                end = Offset(w * 0.52f, h * 0.54f),
                                strokeWidth = 2.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                            drawCircle(
                                color = Color(0xFF131722),
                                radius = 2.2.dp.toPx(),
                                center = Offset(w * 0.57f, h * 0.46f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Brand Name in Grand Hotel Cursive Font with Rainbow Gradient
                RainbowText(
                    text = "Irispop",
                    fontSize = 54.sp,
                    fontFamily = GrandHotelFontFamily,
                    modifier = Modifier.testTag("splash_brand_title")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Creator Signature Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF0F1422).copy(alpha = 0.85f))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFFBC2EB).copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "by ",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Muhammad Ashraf",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        style = androidx.compose.ui.text.TextStyle(
                            brush = IrisRainbowBrush
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tagline
                Text(
                    text = "CONNECT • SHARE • VIBE",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Explore App Action Button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF161C2C)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = Color(0xFF2E3852)
                    ),
                    modifier = Modifier.testTag("explore_app_button")
                ) {
                    Text(
                        text = "Explore App",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
