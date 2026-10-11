package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.SavedSong
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.IrisBorderRainbowBrush
import com.example.ui.theme.RainbowCyan
import com.example.ui.theme.RainbowPink
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    savedSongs: List<SavedSong>,
    isSpotifyConnected: Boolean = true,
    spotifyUsername: String = "Irispop Resident (Premium)",
    onEditProfileClick: () -> Unit,
    onShareProfileClick: () -> Unit,
    onOpenChatsClick: () -> Unit = {},
    onManageSpotifyClick: () -> Unit = {},
    onUseSavedSongInPost: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val profilePhotos = listOf(
        "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=200",
        "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=200",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
        "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=200",
        "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=200",
        "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=200"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Rainbow Border
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .border(2.5.dp, IrisBorderRainbowBrush, RoundedCornerShape(26.dp))
                        .padding(3.dp)
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
                        contentDescription = "Your Profile Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(22.dp))
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Name & Follower Stats
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Your Profile",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "@irispop_star",
                        fontSize = 12.sp,
                        color = RainbowPink,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatItem(count = "18", label = "posts")
                        StatItem(count = "5.2k", label = "fans")
                        StatItem(count = "420", label = "following")
                    }
                }
            }
        }

        // Bio Text
        item {
            Text(
                text = "Exploring color, light, and holographic art on the official Irispop network. ✨",
                fontSize = 12.sp,
                color = Color(0xFFD1D5DB),
                lineHeight = 17.sp
            )
        }

        // Edit Profile, Share Profile, and Direct Messages Action Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onEditProfileClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2536)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C374E)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("edit_profile_button")
                ) {
                    Text(
                        text = "Edit Profile",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                Button(
                    onClick = onOpenChatsClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2536)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C374E)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("profile_chats_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Chats",
                        tint = RainbowPink,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Chats",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                Button(
                    onClick = onShareProfileClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2536)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C374E)),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("share_profile_button")
                ) {
                    Text(
                        text = "Share",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
            }
        }

        // Spotify Integration Status Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF141926))
                    .border(1.2.dp, SpotifyGreen.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                    .padding(14.dp)
                    .testTag("profile_spotify_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(SpotifyGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "♫", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Spotify Account",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SpotifyGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isSpotifyConnected) "CONNECTED" else "DISCONNECTED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SpotifyGreen
                                    )
                                }
                            }
                            Text(
                                text = spotifyUsername,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Button(
                        onClick = onManageSpotifyClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SpotifyGreen),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = "Browse Songs",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        // Saved Soundtracks Library Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF151B2A))
                    .border(1.dp, Color(0xFF242F46), RoundedCornerShape(18.dp))
                    .padding(14.dp)
                    .testTag("saved_audio_library_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Saved Audio",
                            tint = RainbowPink,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Saved Audio Library",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = "${savedSongs.size} ${if (savedSongs.size == 1) "Track" else "Tracks"}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = AccentCyan
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    savedSongs.forEach { song ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F1422))
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = song.title,
                                    fontSize = 12.sp,
                                    color = Color(0xFFE2E8F0),
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Spotify • ${song.duration}",
                                    fontSize = 10.sp,
                                    color = SpotifyGreen
                                )
                            }

                            // Use in post action chip
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SpotifyGreen.copy(alpha = 0.15f))
                                    .border(1.dp, SpotifyGreen, RoundedCornerShape(8.dp))
                                    .clickable { onUseSavedSongInPost(song.title) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .testTag("profile_use_song_${song.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = SpotifyGreen,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Use on Post",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SpotifyGreen
                                )
                            }
                        }
                    }
                }
            }
        }

        // Post Memories Grid (3 Columns)
        item {
            Text(
                text = "POSTS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            // 3-columns photo grid layout
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                profilePhotos.chunked(3).forEach { rowPhotos ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rowPhotos.forEach { photoUrl ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFF1E2536))
                            ) {
                                AsyncImage(
                                    model = photoUrl,
                                    contentDescription = "Post photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        // Fill empty slots if last row has less than 3
                        for (i in rowPhotos.size until 3) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(count: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = count,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}
