package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.RainbowCyan
import com.example.ui.theme.RainbowPink
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsModal(
    isOpen: Boolean,
    rainbowAuraEnabled: Boolean,
    onToggleRainbowAura: (Boolean) -> Unit,
    onReplaySplash: () -> Unit,
    onDismiss: () -> Unit
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
        modifier = Modifier.testTag("settings_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Irispop Settings & Guidelines",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Row(modifier = Modifier.padding(top = 2.dp)) {
                        Text(
                            text = "Created by ",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Text(
                            text = "Muhammad Ashraf",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RainbowPink
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Settings",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Aura Glow Boost Toggle
            Text(
                text = "IRIS RAINBOW AURA",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = RainbowPink,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1A2132))
                    .border(1.dp, Color(0xFF28344E), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pastel Neon Glow Boost",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                    Text(
                        text = "Holographic cards aura illumination",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
                Switch(
                    checked = rainbowAuraEnabled,
                    onCheckedChange = onToggleRainbowAura,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF0F121B),
                        checkedTrackColor = RainbowPink,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = Color(0xFF202A40)
                    ),
                    modifier = Modifier.testTag("aura_boost_switch")
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Rules & Regulations
            Text(
                text = "RULES & REGULATIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = RainbowCyan,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1A2132))
                    .border(1.dp, Color(0xFF28344E), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RuleItem(
                    bullet = "•",
                    bold = "Original Creations: ",
                    text = "Share original photography, music, and digital visual art."
                )
                RuleItem(
                    bullet = "•",
                    bold = "Zero Tolerance for Bullying: ",
                    text = "Content that threatens or demeans users is banned automatically."
                )
                RuleItem(
                    bullet = "•",
                    bold = "Audio Attribution: ",
                    text = "Credit original sound creators when remixing reel tracks."
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Replay Converging Intro Animation Button
            Button(
                onClick = onReplaySplash,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF20293F)
                ),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF384666)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("replay_converging_animation_button")
            ) {
                Text(
                    text = "✨ Replay Converging Intro Animation",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun RuleItem(bullet: String, bold: String, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = bullet,
            fontSize = 12.sp,
            color = AccentCyan,
            modifier = Modifier.padding(end = 6.dp)
        )
        Column {
            Text(
                text = bold + text,
                fontSize = 12.sp,
                color = Color(0xFFCBD5E1),
                lineHeight = 16.sp
            )
        }
    }
}
