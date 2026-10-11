package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CreationType
import com.example.ui.theme.GrandHotelFontFamily
import com.example.ui.theme.IrisRainbowBrush
import com.example.ui.theme.RainbowCyan
import com.example.ui.theme.RainbowPeach
import com.example.ui.theme.RainbowPink
import com.example.ui.theme.RainbowPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onSelectType: (CreationType) -> Unit
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF141926),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF333D54))
            )
        },
        modifier = Modifier.testTag("creator_studio_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .padding(bottom = 28.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Create on ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    RainbowText(
                        text = "Irispop",
                        fontSize = 24.sp,
                        fontFamily = GrandHotelFontFamily
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Creator",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2x2 Grid for Creation options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CreationCard(
                    title = "Post",
                    subtitle = "Share photo grid",
                    emoji = "🖼️",
                    accentColor = RainbowPink,
                    onClick = { onSelectType(CreationType.POST) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("create_post_option")
                )
                CreationCard(
                    title = "Reel",
                    subtitle = "Record 90s clip",
                    emoji = "✊",
                    accentColor = RainbowCyan,
                    onClick = { onSelectType(CreationType.REEL) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("create_reel_option")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CreationCard(
                    title = "Story",
                    subtitle = "24h moments",
                    emoji = "⭕",
                    accentColor = RainbowPeach,
                    onClick = { onSelectType(CreationType.STORY) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("create_story_option")
                )
                CreationCard(
                    title = "Text",
                    subtitle = "Thoughts & audio",
                    emoji = "✍️",
                    accentColor = RainbowPurple,
                    onClick = { onSelectType(CreationType.TEXT) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("create_text_option")
                )
            }
        }
    }
}

@Composable
private fun CreationCard(
    title: String,
    subtitle: String,
    emoji: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF1B2234))
            .border(1.dp, Color(0xFF2C3752), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accentColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}
