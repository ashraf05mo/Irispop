package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.PostItem
import com.example.model.SpotifyTrack
import com.example.model.StoryItem
import com.example.ui.components.FeedPostCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.IrisBorderRainbowBrush
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun FeedScreen(
    stories: List<StoryItem>,
    posts: List<PostItem>,
    auraEnabled: Boolean,
    onOpenStoryCreator: () -> Unit,
    onStoryClick: (StoryItem) -> Unit,
    onLikePost: (String) -> Unit,
    onCommentPost: (String) -> Unit,
    onSendPost: (PostItem) -> Unit,
    onSavePost: (String) -> Unit,
    onPrevImage: (String) -> Unit,
    onNextImage: (String) -> Unit,
    onSaveSong: (String) -> Unit,
    isPlayingPreview: Boolean = false,
    playingTrackId: String? = null,
    onTogglePlayAudio: (String) -> Unit = {},
    onUseSongInPost: (PostItem) -> Unit = {},
    onOpenSpotifyTrack: (SpotifyTrack) -> Unit = {},
    onChatWithAuthor: (String) -> Unit = {},
    onReportPost: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("feed_screen_list"),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stories Tray Header
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // "Your Story" Item with cyan '+' badge
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable(onClick = onOpenStoryCreator)
                            .testTag("your_story_item")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .border(2.dp, IrisBorderRainbowBrush, RoundedCornerShape(18.dp))
                                .padding(2.dp)
                        ) {
                            AsyncImage(
                                model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=160",
                                contentDescription = "Your Story Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                            )

                            // Cyan plus badge
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.BottomEnd)
                                    .clip(CircleShape)
                                    .background(AccentCyan)
                                    .border(2.dp, Color(0xFF0F121B), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F121B)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your Story",
                            fontSize = 10.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Community creator stories
                items(stories, key = { it.id }) { story ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onStoryClick(story) }
                            .testTag("story_item_${story.name}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .border(2.dp, IrisBorderRainbowBrush, RoundedCornerShape(18.dp))
                                .padding(2.dp)
                        ) {
                            AsyncImage(
                                model = story.avatarUrl,
                                contentDescription = "${story.name}'s story",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "@${story.name}",
                            fontSize = 10.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Main Feed Posts
        items(posts, key = { it.id }) { post ->
            FeedPostCard(
                post = post,
                auraEnabled = auraEnabled,
                onLikeClick = { onLikePost(post.id) },
                onCommentClick = { onCommentPost(post.id) },
                onSendClick = { onSendPost(post) },
                onSaveClick = { onSavePost(post.id) },
                onPrevImage = { onPrevImage(post.id) },
                onNextImage = { onNextImage(post.id) },
                onSaveSong = onSaveSong,
                isPlayingPreview = isPlayingPreview,
                playingTrackId = playingTrackId,
                onTogglePlayAudio = onTogglePlayAudio,
                onUseSongInPost = onUseSongInPost,
                onOpenSpotifyTrack = onOpenSpotifyTrack,
                onChatWithAuthor = onChatWithAuthor,
                onReportClick = onReportPost
            )
        }

        // Bottom Spacer for navigation bar breathing room
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
