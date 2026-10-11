package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.model.ChatMessage
import com.example.model.ChatUser
import com.example.model.PostItem
import com.example.model.SpotifyTrack
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.GrandHotelFontFamily
import com.example.ui.theme.IrisBorderRainbowBrush
import com.example.ui.theme.IrisButtonRainbowBrush
import com.example.ui.theme.RainbowCyan
import com.example.ui.theme.RainbowPink
import com.example.ui.theme.RainbowPurple
import com.example.ui.theme.SpotifyGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Direct Messages (Chat) Inbox List Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListSheet(
    isOpen: Boolean,
    chatUsers: List<ChatUser>,
    onDismiss: () -> Unit,
    onSelectUser: (ChatUser) -> Unit,
    onOpenSpotifyCatalog: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }

    val filteredUsers = remember(searchQuery, chatUsers) {
        if (searchQuery.isBlank()) chatUsers
        else chatUsers.filter {
            it.handle.contains(searchQuery, ignoreCase = true) ||
                it.name.contains(searchQuery, ignoreCase = true)
        }
    }

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
        modifier = modifier.testTag("chat_inbox_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
                        text = "Irispop ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    RainbowText(
                        text = "Direct Messages",
                        fontSize = 24.sp,
                        fontFamily = GrandHotelFontFamily
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Chats",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Spotify music sync banner in chat
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF161F2E))
                    .border(1.dp, SpotifyGreen.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .clickable { onOpenSpotifyCatalog() }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(SpotifyGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "♫", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Spotify Audio Sharing Enabled",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Send tracks in DMs & use friend's songs in posts",
                            fontSize = 10.sp,
                            color = SpotifyGreen
                        )
                    }
                }
                Text(text = "Explore ›", fontSize = 11.sp, color = SpotifyGreen, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search user
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Irispop creators...", fontSize = 12.sp, color = TextMuted) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = TextSecondary)
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RainbowCyan,
                    unfocusedBorderColor = Color(0xFF232B3E),
                    focusedContainerColor = Color(0xFF161C2A),
                    unfocusedContainerColor = Color(0xFF161C2A),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("chat_search_user_input")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Users Conversation List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .testTag("chat_users_list"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredUsers, key = { it.id }) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF161C2C))
                            .border(1.dp, Color(0xFF222B42), RoundedCornerShape(16.dp))
                            .clickable { onSelectUser(user) }
                            .padding(12.dp)
                            .testTag("chat_user_item_${user.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Avatar with Online Dot
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .border(1.8.dp, IrisBorderRainbowBrush, CircleShape)
                                .padding(2.dp)
                        ) {
                            AsyncImage(
                                model = user.avatar,
                                contentDescription = user.handle,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )

                            if (user.isOnline) {
                                Box(
                                    modifier = Modifier
                                        .size(13.dp)
                                        .align(Alignment.BottomEnd)
                                        .clip(CircleShape)
                                        .background(AccentCyan)
                                        .border(2.dp, Color(0xFF101420), CircleShape)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // User names & last message
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = user.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = user.lastMessageTime,
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }

                            Text(
                                text = user.handle,
                                fontSize = 11.sp,
                                color = RainbowPink,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = user.lastMessage,
                                fontSize = 12.sp,
                                color = if (user.unreadCount > 0) Color.White else TextSecondary,
                                fontWeight = if (user.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                                maxLines = 1
                            )

                            // User's Spotify song pill
                            if (user.currentTrackTitle.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(3.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF0F1420))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = null,
                                        tint = SpotifyGreen,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = user.currentTrackTitle,
                                        fontSize = 10.sp,
                                        color = SpotifyGreen,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        // Unread Badge
                        if (user.unreadCount > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(RainbowPink),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.unreadCount.toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 1-on-1 Interactive Conversation Screen / Modal
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatConversationSheet(
    isOpen: Boolean,
    user: ChatUser?,
    messages: List<ChatMessage>,
    isOtherUserTyping: Boolean,
    isPlaying: Boolean,
    playingTrackId: String?,
    onDismiss: () -> Unit,
    onSendMessage: (String, SpotifyTrack?) -> Unit,
    onReactToMessage: (String, String) -> Unit,
    onTogglePlayAudio: (String) -> Unit,
    onUseSongInPost: (SpotifyTrack) -> Unit,
    onOpenSpotifyPicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen || user == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size, isOtherUserTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val quickReplies = listOf(
        "Love this soundtrack! 🎶",
        "What's your Spotify link?",
        "Use this on my post! ✨",
        "Let's collab on Irispop 🌈"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF0D101A),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF333D54))
            )
        },
        modifier = modifier.testTag("chat_conversation_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
        ) {
            // Conversation Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF141926))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss, modifier = Modifier.size(34.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Avatar
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .border(1.5.dp, IrisBorderRainbowBrush, CircleShape)
                        .padding(2.dp)
                ) {
                    AsyncImage(
                        model = user.avatar,
                        contentDescription = user.handle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Name & Status
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user.handle,
                            fontSize = 11.sp,
                            color = RainbowPink
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isOtherUserTyping) "typing..." else if (user.isOnline) "• Online" else "• Offline",
                            fontSize = 10.sp,
                            color = if (isOtherUserTyping) RainbowCyan else if (user.isOnline) AccentCyan else TextMuted,
                            fontWeight = if (isOtherUserTyping) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                // Spotify Now Playing Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1B2332))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = SpotifyGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Spotify", fontSize = 10.sp, color = SpotifyGreen, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Messages Stream
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("chat_messages_stream"),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    ChatMessageBubble(
                        message = msg,
                        isPlaying = isPlaying && playingTrackId == msg.attachedTrack?.id,
                        onTogglePlay = { trackId -> onTogglePlayAudio(trackId) },
                        onUseSongInPost = onUseSongInPost,
                        onReact = { emoji -> onReactToMessage(msg.id, emoji) }
                    )
                }

                // Typing Indicator
                if (isOtherUserTyping) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF181F2E))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${user.handle} is typing ✨ ...",
                                fontSize = 11.sp,
                                color = RainbowPink,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Quick suggestion reply chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickReplies) { chip ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF181F2E))
                            .border(1.dp, Color(0xFF28344B), RoundedCornerShape(10.dp))
                            .clickable {
                                onSendMessage(chip, null)
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(text = chip, fontSize = 11.sp, color = RainbowCyan)
                    }
                }
            }

            // Interactive Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Attach Spotify Track Button (+)
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1B2332))
                        .border(1.dp, SpotifyGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable { onOpenSpotifyPicker() }
                        .testTag("chat_attach_spotify_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Attach Spotify Track",
                        tint = SpotifyGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Text Field
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = {
                        Text("Message ${user.handle}...", fontSize = 13.sp, color = TextMuted)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RainbowPink,
                        unfocusedBorderColor = Color(0xFF253046),
                        focusedContainerColor = Color(0xFF161C2A),
                        unfocusedContainerColor = Color(0xFF161C2A),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_message_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Send Button with Iris Rainbow Glow
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(IrisButtonRainbowBrush)
                        .clickable {
                            if (textInput.isNotBlank()) {
                                onSendMessage(textInput, null)
                                textInput = ""
                            }
                        }
                        .testTag("chat_send_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Message",
                        tint = Color(0xFF0F121B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Message Bubble supporting text, reactions, and embedded Spotify tracks
 */
@Composable
fun ChatMessageBubble(
    message: ChatMessage,
    isPlaying: Boolean,
    onTogglePlay: (String) -> Unit,
    onUseSongInPost: (SpotifyTrack) -> Unit,
    onReact: (String) -> Unit
) {
    var showReactionPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isMine) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = if (message.isMine) Alignment.End else Alignment.Start
            ) {
                // Main Bubble
                Box(
                    modifier = Modifier
                        .widthIn(max = 290.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (message.isMine) 16.dp else 4.dp,
                                bottomEnd = if (message.isMine) 4.dp else 16.dp
                            )
                        )
                        .background(
                            if (message.isMine) {
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF9333EA),
                                        Color(0xFFEC4899),
                                        Color(0xFFF43F5E)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF1B2234),
                                        Color(0xFF141A28)
                                    )
                                )
                            }
                        )
                        .border(
                            1.dp,
                            if (message.isMine) Color.White.copy(alpha = 0.25f) else Color(0xFF28344C),
                            RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (message.isMine) 16.dp else 4.dp,
                                bottomEnd = if (message.isMine) 4.dp else 16.dp
                            )
                        )
                        .clickable { showReactionPicker = !showReactionPicker }
                        .padding(12.dp)
                ) {
                    Column {
                        // Text content
                        if (message.text.isNotEmpty()) {
                            Text(
                                text = message.text,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )
                        }

                        // ATTACHED SPOTIFY TRACK CARD
                        if (message.attachedTrack != null) {
                            val track = message.attachedTrack
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0F1420).copy(alpha = 0.9f))
                                    .border(1.dp, SpotifyGreen.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                    .padding(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Album art with Play button
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    ) {
                                        AsyncImage(
                                            model = track.albumArt,
                                            contentDescription = track.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )

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
                                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = "Play track",
                                                tint = SpotifyGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = track.title,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = "${track.artist} • Spotify",
                                            fontSize = 10.sp,
                                            color = SpotifyGreen,
                                            maxLines = 1
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Prominent "Use This Song on Post" button right in chat!
                                Button(
                                    onClick = { onUseSongInPost(track) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SpotifyGreen),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(30.dp)
                                        .testTag("chat_use_song_btn_${track.id}")
                                ) {
                                    Text(
                                        text = "⚡ Use Song in My Post",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }
                        }

                        // ATTACHED POST PREVIEW
                        if (message.attachedPostImage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                AsyncImage(
                                    model = message.attachedPostImage,
                                    contentDescription = "Attached post",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        text = "Post soundtrack attached 🎶",
                                        fontSize = 10.sp,
                                        color = RainbowCyan,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Timestamp
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = message.timestamp,
                                fontSize = 9.sp,
                                color = if (message.isMine) Color.White.copy(alpha = 0.7f) else TextMuted
                            )
                        }
                    }
                }

                // Reaction Badge
                if (message.reaction != null) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E2638))
                            .border(1.dp, Color(0xFF2F3C55), RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = message.reaction, fontSize = 12.sp)
                    }
                }

                // Quick emoji picker popup
                AnimatedVisibility(
                    visible = showReactionPicker,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF141926))
                            .border(1.dp, Color(0xFF28344C), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("❤️", "✨", "🔥", "🌈", "🎵").forEach { emoji ->
                            Text(
                                text = emoji,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .clickable {
                                        onReact(emoji)
                                        showReactionPicker = false
                                    }
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Share Post & Spotify Soundtrack to Chat Dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharePostToChatSheet(
    isOpen: Boolean,
    post: PostItem?,
    users: List<ChatUser>,
    onDismiss: () -> Unit,
    onSendToUser: (PostItem, ChatUser) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen || post == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF111522),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF333D54))
            )
        },
        modifier = modifier.testTag("share_post_to_chat_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            Text(
                text = "Share Post & Spotify Audio in Chat",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Send this post and song '${post.songTitle}' to a friend",
                fontSize = 11.sp,
                color = RainbowPink
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Post preview snippet
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF181F2E))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = post.images.firstOrNull() ?: "",
                    contentDescription = "Post preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorHandle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Song: ${post.songTitle}",
                        fontSize = 11.sp,
                        color = SpotifyGreen,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Send To Creator:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                users.forEach { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF161C2A))
                            .clickable { onSendToUser(post, user) }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(1.2.dp, IrisBorderRainbowBrush, CircleShape)
                                    .padding(1.dp)
                            ) {
                                AsyncImage(
                                    model = user.avatar,
                                    contentDescription = user.handle,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = user.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = user.handle,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Button(
                            onClick = { onSendToUser(post, user) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RainbowPink),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(text = "Send", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
