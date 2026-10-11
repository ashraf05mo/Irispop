package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.PostItem
import com.example.model.SpotifyCatalog
import com.example.model.SpotifyTrack
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentRose
import com.example.ui.theme.IrisBorderRainbowBrush
import com.example.ui.theme.RainbowPink
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun FeedPostCard(
    post: PostItem,
    auraEnabled: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onSendClick: () -> Unit,
    onSaveClick: () -> Unit,
    onPrevImage: () -> Unit,
    onNextImage: () -> Unit,
    onSaveSong: (String) -> Unit,
    isPlayingPreview: Boolean = false,
    playingTrackId: String? = null,
    onTogglePlayAudio: (String) -> Unit = {},
    onUseSongInPost: (PostItem) -> Unit = {},
    onOpenSpotifyTrack: (SpotifyTrack) -> Unit = {},
    onChatWithAuthor: (String) -> Unit = {},
    onReportClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showContextMenu by remember { mutableStateOf(false) }
    var showHeartPop by remember { mutableStateOf(false) }

    val resolvedSpotifyTrack = remember(post) {
        post.spotifyTrack ?: SpotifyCatalog.findByTitle(post.songTitle)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .rainbowCardBorder(auraEnabled = auraEnabled, shape = RoundedCornerShape(26.dp))
            .padding(14.dp)
            .testTag("post_card_${post.id}")
    ) {
        // Author Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, start = 2.dp, end = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onChatWithAuthor(post.authorHandle) }
                    .padding(2.dp)
            ) {
                // Rainbow ring avatar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.8.dp, IrisBorderRainbowBrush, CircleShape)
                        .padding(2.dp)
                ) {
                    AsyncImage(
                        model = post.authorAvatar,
                        contentDescription = "Author Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = post.authorHandle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Tap to chat 💬",
                        fontSize = 9.sp,
                        color = RainbowPink
                    )
                }
            }

            IconButton(
                onClick = { showContextMenu = !showContextMenu },
                modifier = Modifier
                    .size(36.dp)
                    .testTag("post_menu_btn_${post.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Post Options",
                    tint = RainbowPink,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Media Area (4:5 Aspect Ratio) with Double-Tap To Like & Carousels
        val currentImageUrl = post.images.getOrElse(post.currentImageIndex) {
            post.images.firstOrNull() ?: ""
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF0F121C))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            showHeartPop = true
                            onLikeClick()
                        }
                    )
                }
                .testTag("post_media_${post.id}"),
            contentAlignment = Alignment.Center
        ) {
            // Main image
            AsyncImage(
                model = currentImageUrl,
                contentDescription = "Post Media Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Soft pastel rainbow gradient veil
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color(0x22581C87),
                                Color(0xAA000000)
                            )
                        )
                    )
            )

            // Double tap pop heart animation
            HeartBurstOverlay(
                visible = showHeartPop,
                onAnimationEnd = { showHeartPop = false }
            )

            // Carousel Chevrons
            if (post.images.size > 1) {
                // Left
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(onClick = onPrevImage),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous Photo",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Right
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(onClick = onNextImage),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next Photo",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Photo indicator dots
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    post.images.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .size(if (index == post.currentImageIndex) 6.dp else 4.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == post.currentImageIndex) RainbowPink else Color.White.copy(alpha = 0.5f)
                                )
                        )
                    }
                }
            }

            // FLOATING CONTEXTUAL POPUP MENU (Like, Comment, Save Song, Report)
            androidx.compose.animation.AnimatedVisibility(
                visible = showContextMenu,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .width(180.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF141A28).copy(alpha = 0.96f))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(18.dp))
                        .padding(6.dp)
                        .testTag("floating_card_menu")
                ) {
                    ContextMenuItem(
                        text = "Use Song on Post",
                        icon = Icons.Default.Add,
                        iconTint = SpotifyGreen,
                        onClick = {
                            showContextMenu = false
                            onUseSongInPost(post)
                        }
                    )
                    ContextMenuItem(
                        text = "Spotify Track Info",
                        icon = Icons.Default.MusicNote,
                        iconTint = SpotifyGreen,
                        onClick = {
                            showContextMenu = false
                            onOpenSpotifyTrack(resolvedSpotifyTrack)
                        }
                    )
                    ContextMenuItem(
                        text = "Chat with Creator",
                        icon = Icons.Default.Chat,
                        iconTint = RainbowPink,
                        onClick = {
                            showContextMenu = false
                            onChatWithAuthor(post.authorHandle)
                        }
                    )
                    ContextMenuItem(
                        text = if (post.isLiked) "Unlike" else "Like",
                        icon = Icons.Default.Favorite,
                        iconTint = AccentRose,
                        onClick = {
                            showContextMenu = false
                            onLikeClick()
                        }
                    )
                    ContextMenuItem(
                        text = "Comment",
                        icon = Icons.Default.ChatBubbleOutline,
                        iconTint = AccentCyan,
                        onClick = {
                            showContextMenu = false
                            onCommentClick()
                        }
                    )
                    ContextMenuItem(
                        text = "Save Song",
                        icon = Icons.Default.MusicNote,
                        iconTint = AccentAmber,
                        onClick = {
                            showContextMenu = false
                            onSaveSong(post.songTitle)
                        }
                    )
                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.1f),
                        thickness = 0.8.dp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    ContextMenuItem(
                        text = "Report",
                        icon = Icons.Default.Flag,
                        iconTint = AccentRose,
                        onClick = {
                            showContextMenu = false
                            onReportClick(post.authorHandle)
                        }
                    )
                }
            }
        }

        // Bottom Action Bar (Like, Comment, Send, Save)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(22.dp)) {
                // Like
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(onClick = onLikeClick)
                        .testTag("feed_like_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) AccentRose else TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = if (post.isLiked) "Liked" else "Like",
                        fontSize = 10.sp,
                        color = if (post.isLiked) AccentRose else TextMuted,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Comment
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(onClick = onCommentClick)
                        .testTag("feed_comment_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Comment",
                        tint = TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Comment",
                        fontSize = 10.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // Send
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(onClick = onSendClick)
                        .testTag("feed_send_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Send",
                        fontSize = 10.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Save
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(onClick = onSaveClick)
                    .testTag("feed_save_btn_${post.id}")
            ) {
                Icon(
                    imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Save Bookmark",
                    tint = if (post.isSaved) AccentPurple else TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = if (post.isSaved) "Saved" else "Save",
                    fontSize = 10.sp,
                    color = if (post.isSaved) AccentPurple else TextMuted,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Caption & Engagement Numbers
        Column(modifier = Modifier.padding(top = 10.dp, start = 4.dp, end = 4.dp)) {
            Text(
                text = "${"%,d".format(post.likesCount)} likes",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            val captionText = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
                    append(post.authorHandle)
                    append(" ")
                }
                append(post.caption)
            }

            Text(
                text = captionText,
                fontSize = 12.sp,
                color = Color(0xFFD1D5DB),
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Spotify Audio Track and "Use Song" Action Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isThisTrackPlaying = isPlayingPreview && playingTrackId == resolvedSpotifyTrack.id

                // Spotify Song pill (clickable to open Spotify detail sheet)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF161F2E))
                        .border(1.dp, if (isThisTrackPlaying) SpotifyGreen else SpotifyGreen.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .clickable { onOpenSpotifyTrack(resolvedSpotifyTrack) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("post_spotify_pill_${post.id}")
                ) {
                    // Play / Pause Icon Button to hear from speaker
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(SpotifyGreen)
                            .clickable { onTogglePlayAudio(resolvedSpotifyTrack.id) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isThisTrackPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isThisTrackPlaying) "Pause" else "Play",
                            tint = Color.Black,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = post.songTitle,
                        fontSize = 11.sp,
                        color = if (isThisTrackPlaying) SpotifyGreen else Color(0xFFE2E8F0),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // "Use Song" Button on this post
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SpotifyGreen.copy(alpha = 0.15f))
                        .border(1.dp, SpotifyGreen, RoundedCornerShape(8.dp))
                        .clickable { onUseSongInPost(post) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("post_use_song_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Use song",
                        tint = SpotifyGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Use Song",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpotifyGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // View all comments
            Text(
                text = "View all ${post.commentsCount} comments",
                fontSize = 11.sp,
                color = TextMuted,
                modifier = Modifier
                    .clickable(onClick = onCommentClick)
                    .padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun ContextMenuItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
        )
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconTint,
            modifier = Modifier.size(16.dp)
        )
    }
}
