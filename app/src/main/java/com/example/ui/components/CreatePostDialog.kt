package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Refresh
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
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Create Post Dialog / Sheet with Spotify Soundtrack integration
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostDialog(
    isOpen: Boolean,
    initialCaption: String,
    initialSong: SpotifyTrack?,
    initialImageUrl: String,
    onDismiss: () -> Unit,
    onOpenSpotifyPicker: () -> Unit,
    onPublish: (caption: String, imageUrl: String, song: SpotifyTrack) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var caption by remember(initialCaption) { mutableStateOf(initialCaption) }
    var selectedImage by remember(initialImageUrl) { mutableStateOf(initialImageUrl) }
    var currentSong by remember(initialSong) { mutableStateOf(initialSong) }

    val presetImages = listOf(
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800",
        "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800",
        "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=800",
        "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800",
        "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=800",
        "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800"
    )

    val hashtagPills = listOf("#irispop", "#spotify", "#rainbowglow", "#holographic", "#synthwave")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF101420),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF333D54))
            )
        },
        modifier = modifier.testTag("create_post_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "New Post with ",
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

                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ATTACHED SPOTIFY SOUNDTRACK BANNER
            if (currentSong != null) {
                val song = currentSong!!
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF161F2E))
                        .border(1.5.dp, SpotifyGreen.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                        .testTag("attached_spotify_track_card"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Album art with Spotify badge
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        AsyncImage(
                            model = song.albumArt,
                            contentDescription = song.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(SpotifyGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "♫", fontSize = 9.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Spotify Soundtrack:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SpotifyGreen
                            )
                        }
                        Text(
                            text = song.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = "${song.artist} • ${song.duration}",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1
                        )
                    }

                    // Change track button
                    Button(
                        onClick = onOpenSpotifyPicker,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF232D42)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("change_spotify_track_btn")
                    ) {
                        Text(
                            text = "Change",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RainbowPink
                        )
                    }
                }
            } else {
                // No song attached yet prompt
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF161F2E))
                        .border(1.dp, Color(0xFF2A364E), RoundedCornerShape(14.dp))
                        .clickable { onOpenSpotifyPicker() }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = SpotifyGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Attach Spotify Song to Post", fontSize = 13.sp, color = TextPrimary)
                    }
                    Text(text = "+ Choose Track", fontSize = 12.sp, color = SpotifyGreen, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Photo Selection Preview
            Text(
                text = "Select Photo:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(presetImages) { imgUrl ->
                    val isSelected = selectedImage == imgUrl
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) RainbowPink else Color(0xFF2B364C),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedImage = imgUrl }
                    ) {
                        AsyncImage(
                            model = imgUrl,
                            contentDescription = "Post photo choice",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(3.dp)
                                    .clip(CircleShape)
                                    .background(RainbowPink),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.Black,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Caption Input
            OutlinedTextField(
                value = caption,
                onValueChange = { caption = it },
                placeholder = {
                    Text("Write a caption for your Irispop post...", fontSize = 13.sp, color = TextMuted)
                },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RainbowPink,
                    unfocusedBorderColor = Color(0xFF263248),
                    focusedContainerColor = Color(0xFF151B28),
                    unfocusedContainerColor = Color(0xFF151B28),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_post_caption_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Hashtag Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(hashtagPills) { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF192132))
                            .clickable {
                                if (!caption.contains(tag)) {
                                    caption = if (caption.isBlank()) tag else "$caption $tag"
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = tag, fontSize = 11.sp, color = RainbowCyan)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Publish Button with Rainbow Gradient
            Button(
                onClick = {
                    val finalSong = currentSong ?: initialSong ?: com.example.model.SpotifyCatalog.tracks.first()
                    onPublish(caption, selectedImage, finalSong)
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(IrisButtonRainbowBrush)
                    .testTag("publish_post_submit_btn")
            ) {
                Text(
                    text = "Publish to Irispop ✨",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F121B)
                )
            }
        }
    }
}
