package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GrandHotelFontFamily
import com.example.ui.theme.IrisBorderRainbowBrush
import com.example.ui.theme.IrisSurface
import com.example.ui.theme.RainbowPink
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.TextPrimary

@Composable
fun IrispopTopBar(
    unreadChatCount: Int = 0,
    isSpotifyConnected: Boolean = true,
    onReplaySplash: () -> Unit,
    onTitleClick: () -> Unit,
    onChatClick: () -> Unit,
    onSpotifyClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(IrisSurface.copy(alpha = 0.95f))
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Micro Logo Sparkle button & Spotify Connect indicator
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .border(1.5.dp, IrisBorderRainbowBrush, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF121622))
                    .clickable(onClick = onReplaySplash)
                    .testTag("replay_splash_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "✨", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Spotify status chip
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF161C2A))
                    .border(1.dp, if (isSpotifyConnected) SpotifyGreen.copy(alpha = 0.6f) else Color(0xFF273146), RoundedCornerShape(10.dp))
                    .clickable(onClick = onSpotifyClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("top_bar_spotify_btn")
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(if (isSpotifyConnected) SpotifyGreen else Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "♫", fontSize = 8.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (isSpotifyConnected) "Spotify" else "Connect",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSpotifyConnected) SpotifyGreen else Color.Gray
                )
            }
        }

        // Center: Grand Hotel cursive brand title with rainbow gradient
        RainbowText(
            text = "Irispop",
            fontSize = 30.sp,
            fontFamily = GrandHotelFontFamily,
            modifier = Modifier
                .clickable(onClick = onTitleClick)
                .testTag("top_bar_title")
        )

        // Right: Chat (Direct Messages) & 3-dots Settings menu
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Direct Messages Icon Button with unread badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = onChatClick)
                    .testTag("top_bar_chat_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Chat Direct Messages",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )

                if (unreadChatCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 4.dp, end = 4.dp)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(RainbowPink),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = unreadChatCount.toString(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("settings_menu_button")
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Settings and Community Guidelines",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
