package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.SpotifyTrack
import com.example.ui.theme.GrandHotelFontFamily
import com.example.ui.theme.IrisBorderRainbowBrush
import com.example.ui.theme.IrisButtonRainbowBrush
import com.example.ui.theme.RainbowCyan
import com.example.ui.theme.RainbowPink
import com.example.ui.theme.SpotifyCardBg
import com.example.ui.theme.SpotifyDark
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Animated Spotify Audio Waveform Visualizer
 */
@Composable
fun AudioWaveformVisualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barColor: Color = SpotifyGreen
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")
    val anim1 by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(tween(420, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val anim2 by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(tween(560, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val anim3 by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(380, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b3"
    )
    val anim4 by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(tween(490, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b4"
    )

    Row(
        modifier = modifier.height(18.dp),
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val heights = if (isPlaying) listOf(anim1, anim2, anim3, anim4) else listOf(0.35f, 0.45f, 0.3f, 0.5f)
        heights.forEach { fraction ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((18 * fraction).dp.coerceAtLeast(3.dp))
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(barColor)
            )
        }
    }
}

/**
 * Spotify Track Detail & Action Sheet
 * Allows user to:
 * - Preview song with audio visualizer
 * - "Use This Song on Post" (prepares post creator with this song)
 * - "Send in Chat"
 * - "Save to Profile Library"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyTrackDetailSheet(
    isOpen: Boolean,
    track: SpotifyTrack?,
    isPlaying: Boolean,
    onDismiss: () -> Unit,
    onTogglePlay: (String) -> Unit,
    onUseSongInPost: (SpotifyTrack) -> Unit,
    onSendInChat: (SpotifyTrack) -> Unit,
    onSaveSong: (SpotifyTrack) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen || track == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF11141E),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF333D54))
            )
        },
        modifier = modifier.testTag("spotify_track_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with Spotify Branding
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(SpotifyGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "♫", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Spotify Soundtrack",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Large Album Art with Holographic Halo
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .border(2.5.dp, IrisBorderRainbowBrush, RoundedCornerShape(26.dp))
                    .padding(3.dp)
            ) {
                AsyncImage(
                    model = track.albumArt,
                    contentDescription = track.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(164.dp)
                        .clip(RoundedCornerShape(22.dp))
                )

                // Play / Pause Floating Overlay
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(1.5.dp, SpotifyGreen, CircleShape)
                        .clickable { onTogglePlay(track.id) }
                        .testTag("spotify_preview_play_toggle"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = SpotifyGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Track title & artist
            Text(
                text = track.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )
            Text(
                text = "${track.artist} • ${track.album}",
                fontSize = 13.sp,
                color = SpotifyGreen,
                fontWeight = FontWeight.Medium
            )

            // Audio wave bar during preview
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF181F2E))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AudioWaveformVisualizer(isPlaying = isPlaying)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isPlaying) "Playing Spotify 30s preview..." else "30s high-fidelity audio preview",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // PRIMARY HERO CTA: "Use This Song on My Post"
            Button(
                onClick = {
                    onDismiss()
                    onUseSongInPost(track)
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SpotifyGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("spotify_use_song_in_post_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Use This Song on Post",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // SECONDARY ACTIONS (Send in Chat & Save to Profile)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        onDismiss()
                        onSendInChat(track)
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2638)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E3A54)),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("spotify_send_in_chat_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        tint = RainbowPink,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Share in Chat",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                Button(
                    onClick = {
                        onSaveSong(track)
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2638)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E3A54)),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("spotify_save_to_library_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = RainbowCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Save Track",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

/**
 * Spotify Music Picker Sheet for Post Creation
 * Allows searching and choosing any Spotify track to attach to a post.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyMusicPickerSheet(
    isOpen: Boolean,
    tracks: List<SpotifyTrack>,
    selectedTrackId: String?,
    isPlaying: Boolean,
    playingTrackId: String?,
    onDismiss: () -> Unit,
    onTogglePlay: (String) -> Unit,
    onSelectTrack: (SpotifyTrack) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("All") }

    val genres = listOf("All", "Trending", "Synthwave", "Chill", "Lo-Fi", "Irispop")

    val filteredTracks = remember(searchQuery, selectedGenre, tracks) {
        tracks.filter { track ->
            val matchQuery = searchQuery.isBlank() ||
                track.title.contains(searchQuery, ignoreCase = true) ||
                track.artist.contains(searchQuery, ignoreCase = true)
            val matchGenre = selectedGenre == "All" ||
                track.previewAudioHint.contains(selectedGenre, ignoreCase = true) ||
                track.album.contains(selectedGenre, ignoreCase = true)
            matchQuery && matchGenre
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF11141E),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF333D54))
            )
        },
        modifier = modifier.testTag("spotify_music_picker_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(SpotifyGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "♫", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Choose Spotify Track",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search songs, artists, or vibes...", fontSize = 13.sp, color = TextMuted) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = SpotifyGreen)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SpotifyGreen,
                    unfocusedBorderColor = Color(0xFF273146),
                    focusedContainerColor = Color(0xFF161C2A),
                    unfocusedContainerColor = Color(0xFF161C2A),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("spotify_picker_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Genre Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(genres) { genre ->
                    val isSelected = selectedGenre == genre
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) SpotifyGreen else Color(0xFF1B2333))
                            .clickable { selectedGenre = genre }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = genre,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.Black else TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Track List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .testTag("spotify_picker_track_list"),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTracks, key = { it.id }) { track ->
                    val isSelected = track.id == selectedTrackId
                    val isThisPlaying = isPlaying && playingTrackId == track.id

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) Color(0xFF1D283C) else Color(0xFF141926))
                            .border(
                                width = 1.dp,
                                color = if (isSelected) SpotifyGreen else Color(0xFF222B3D),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onSelectTrack(track) }
                            .padding(10.dp)
                            .testTag("spotify_picker_item_${track.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Album art with Play button
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            AsyncImage(
                                model = track.albumArt,
                                contentDescription = track.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(46.dp)
                            )

                            // Mini play/pause button
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .clickable { onTogglePlay(track.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isThisPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play preview",
                                    tint = SpotifyGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Info
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = track.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                if (isThisPlaying) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    AudioWaveformVisualizer(isPlaying = true)
                                }
                            }

                            Text(
                                text = "${track.artist} • ${track.duration}",
                                fontSize = 11.sp,
                                color = TextMuted,
                                maxLines = 1
                            )
                        }

                        // Select Button
                        Button(
                            onClick = { onSelectTrack(track) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) SpotifyGreen else Color(0xFF232D42)
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = if (isSelected) "Attached ✓" else "Use Track",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
