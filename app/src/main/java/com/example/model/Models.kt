package com.example.model

enum class TabType {
    FEED, REELS, SEARCH, PROFILE
}

data class SpotifyTrack(
    val id: String,
    val title: String,
    val artist: String,
    val album: String = "Irispop Spectrum",
    val albumArt: String = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=300",
    val duration: String = "2:45",
    val spotifyUri: String = "spotify:track:irispop",
    val previewAudioHint: String = "Synth Prism Wave",
    val isPopular: Boolean = false
)

data class StoryItem(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val hasUnseen: Boolean = true
)

data class PostItem(
    val id: String,
    val authorHandle: String,
    val authorAvatar: String,
    val images: List<String>,
    val currentImageIndex: Int = 0,
    val caption: String,
    val songTitle: String,
    val spotifyTrack: SpotifyTrack? = null,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val commentsCount: Int = 248,
    val timeAgo: String = "2h ago"
)

data class ReelItem(
    val id: String,
    val authorHandle: String,
    val authorAvatar: String,
    val mediaUrl: String,
    val caption: String,
    val songTitle: String,
    val spotifyTrack: SpotifyTrack? = null,
    val likesCount: String = "48.2k",
    val commentsCount: String = "1,219",
    val isLiked: Boolean = false,
    val isFollowing: Boolean = false
)

data class CommentItem(
    val id: String,
    val authorHandle: String,
    val authorAvatar: String,
    val text: String,
    val timeAgo: String = "Just now",
    val likesCount: Int = 0
)

data class SavedSong(
    val id: String,
    val title: String,
    val duration: String = "0:30",
    val isNew: Boolean = false,
    val spotifyTrack: SpotifyTrack? = null
)

data class ExploreItem(
    val id: String,
    val titleOrTag: String,
    val imageUrl: String,
    val category: String = "all"
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderHandle: String,
    val text: String,
    val timestamp: String = "Just now",
    val isMine: Boolean,
    val attachedTrack: SpotifyTrack? = null,
    val attachedPostId: String? = null,
    val attachedPostImage: String? = null,
    val reaction: String? = null
)

data class ChatUser(
    val id: String,
    val handle: String,
    val name: String,
    val avatar: String,
    val bio: String = "Irispop creator",
    val isOnline: Boolean = true,
    val lastMessage: String = "",
    val lastMessageTime: String = "Just now",
    val unreadCount: Int = 0,
    val currentTrackTitle: String = "Rainbow Frequency"
)

enum class CreationType(val title: String, val subtitle: String, val emoji: String) {
    POST("Post", "Share photo grid with Spotify audio", "🖼️"),
    REEL("Reel", "Record 90s clip", "✊"),
    STORY("Story", "24h moments", "⭕"),
    TEXT("Text", "Thoughts & audio", "✍️")
}

