package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.IrispopAudioPlayer
import com.example.model.ChatMessage
import com.example.model.ChatUser
import com.example.model.CommentItem
import com.example.model.CreationType
import com.example.model.ExploreItem
import com.example.model.PostItem
import com.example.model.ReelItem
import com.example.model.SavedSong
import com.example.model.SpotifyCatalog
import com.example.model.SpotifyTrack
import com.example.model.StoryItem
import com.example.model.TabType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class IrispopUiState(
    val isSplashVisible: Boolean = true,
    val activeTab: TabType = TabType.FEED,
    val rainbowAuraEnabled: Boolean = true,
    val posts: List<PostItem> = emptyList(),
    val stories: List<StoryItem> = emptyList(),
    val reel: ReelItem = ReelItem(
        id = "reel-1",
        authorHandle = "@solaris_vibe",
        authorAvatar = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=100",
        mediaUrl = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=900",
        caption = "Captured the prism transition during Golden Hour ✨ Double tap anywhere to like!",
        songTitle = "Original Sound - Luminescence Wave 07",
        spotifyTrack = SpotifyCatalog.tracks[1],
        likesCount = "48.2k",
        commentsCount = "1,219",
        isLiked = false,
        isFollowing = false
    ),
    val savedSongs: List<SavedSong> = listOf(
        SavedSong(
            id = "song-1",
            title = "Rainbow Frequency (Original Mix)",
            duration = "3:24",
            spotifyTrack = SpotifyCatalog.tracks[0]
        )
    ),
    val comments: List<CommentItem> = listOf(
        CommentItem(
            id = "c1",
            authorHandle = "@aurora_vibes",
            authorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=80",
            text = "The pastel gradient looks incredible! ❤️",
            timeAgo = "2h ago",
            likesCount = 14
        ),
        CommentItem(
            id = "c2",
            authorHandle = "@soundmaker",
            authorAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=80",
            text = "Saved this audio right now! 🎶",
            timeAgo = "45m ago",
            likesCount = 4
        )
    ),
    val searchQuery: String = "",
    val selectedExploreCategory: String = "All",
    val exploreItems: List<ExploreItem> = emptyList(),
    val isCreatorSheetOpen: Boolean = false,
    val isCommentsSheetOpen: Boolean = false,
    val isSettingsModalOpen: Boolean = false,
    val isReportModalOpen: Boolean = false,
    val reportingUserHandle: String = "@creative_user",
    val toastMessage: String? = null,

    // --- Chat Feature States ---
    val chatUsers: List<ChatUser> = emptyList(),
    val activeChatUser: ChatUser? = null,
    val activeConversationMessages: List<ChatMessage> = emptyList(),
    val isChatListOpen: Boolean = false,
    val isChatConversationOpen: Boolean = false,
    val isShareToChatOpen: Boolean = false,
    val postToShareInChat: PostItem? = null,
    val isOtherUserTyping: Boolean = false,
    val totalUnreadChats: Int = 2,

    // --- Spotify Feature States ---
    val isSpotifyConnected: Boolean = true,
    val spotifyUserName: String = "Irispop Resident",
    val spotifyUserHandle: String = "@spotify_iris",
    val spotifyTracks: List<SpotifyTrack> = SpotifyCatalog.tracks,
    val isSpotifyTrackSheetOpen: Boolean = false,
    val activeSpotifyTrack: SpotifyTrack? = null,
    val isSpotifyPickerOpen: Boolean = false,
    val isPlayingPreview: Boolean = false,
    val playingTrackId: String? = null,

    // --- Create Post Dialog with Spotify Soundtrack ---
    val isCreatePostDialogOpen: Boolean = false,
    val draftPostCaption: String = "",
    val draftPostSong: SpotifyTrack? = null,
    val draftPostImageUrl: String = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800"
)

class IrispopViewModel : ViewModel() {

    private val audioPlayer = IrispopAudioPlayer()
    private val _uiState = MutableStateFlow(IrispopUiState())
    val uiState: StateFlow<IrispopUiState> = _uiState.asStateFlow()

    // Message store by userId
    private val conversationStore = mutableMapOf<String, MutableList<ChatMessage>>()

    init {
        loadInitialData()
        initializeChatData()
        // Auto-dismiss splash after 3 seconds if not dismissed manually
        viewModelScope.launch {
            delay(3200)
            if (_uiState.value.isSplashVisible) {
                _uiState.update { it.copy(isSplashVisible = false) }
            }
        }
    }

    private fun loadInitialData() {
        val initialStories = listOf(
            StoryItem("s1", "neona", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=160"),
            StoryItem("s2", "prism", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=160"),
            StoryItem("s3", "solaris", "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=160"),
            StoryItem("s4", "kai", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=160")
        )

        val initialPosts = listOf(
            PostItem(
                id = "p1",
                authorHandle = "@creative_user",
                authorAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=120",
                images = listOf(
                    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800",
                    "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800",
                    "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=800"
                ),
                currentImageIndex = 0,
                caption = "Immersed in this holographic spectrum atmosphere 🌈 Discover the fresh Irispop frequency. #irispop #rainbowglow #vibes",
                songTitle = "Rainbow Frequency (Original Mix)",
                spotifyTrack = SpotifyCatalog.tracks[0],
                likesCount = 1842,
                isLiked = false,
                isSaved = false,
                commentsCount = 248
            ),
            PostItem(
                id = "p2",
                authorHandle = "@eative_user",
                authorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100",
                images = listOf(
                    "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800",
                    "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800"
                ),
                currentImageIndex = 0,
                caption = "Chromatic waves moving through the twilight city grid ✨ #pastelneon #future",
                songTitle = "Luminescence Wave 07",
                spotifyTrack = SpotifyCatalog.tracks[1],
                likesCount = 924,
                isLiked = false,
                isSaved = false,
                commentsCount = 63
            )
        )

        val initialExplore = listOf(
            ExploreItem("e1", "#neon", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400", "#IrisAesthetic"),
            ExploreItem("e2", "@synth_wave", "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=400", "Creators"),
            ExploreItem("e3", "#pastelart", "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=400", "#IrisAesthetic"),
            ExploreItem("e4", "♫ Velvet Glow", "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=400", "Audio"),
            ExploreItem("e5", "#iridescent", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400", "#IrisAesthetic"),
            ExploreItem("e6", "@aurora_vibes", "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=400", "Creators")
        )

        _uiState.update {
            it.copy(
                stories = initialStories,
                posts = initialPosts,
                exploreItems = initialExplore
            )
        }
    }

    private fun initializeChatData() {
        val users = listOf(
            ChatUser(
                id = "u1",
                handle = "@creative_user",
                name = "Elena Vance",
                avatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=120",
                bio = "Holographic artist & prism designer 🌈",
                isOnline = true,
                lastMessage = "Hey! Use that Spotify song on your next post, it fits so well! ✨",
                lastMessageTime = "2m ago",
                unreadCount = 1,
                currentTrackTitle = "Rainbow Frequency (Original Mix)"
            ),
            ChatUser(
                id = "u2",
                handle = "@solaris_vibe",
                name = "Kai Solaris",
                avatar = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=100",
                bio = "Pastel chillstep producer & visualist 🎵",
                isOnline = true,
                lastMessage = "Sent you the new soundtrack preview!",
                lastMessageTime = "15m ago",
                unreadCount = 1,
                currentTrackTitle = "Luminescence Wave 07"
            ),
            ChatUser(
                id = "u3",
                handle = "@aurora_vibes",
                name = "Maya Aurora",
                avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=80",
                bio = "Chromatic aesthetics & neon photography",
                isOnline = false,
                lastMessage = "Thanks for loving the reel! 💜",
                lastMessageTime = "1h ago",
                unreadCount = 0,
                currentTrackTitle = "Midnight Iridescence"
            ),
            ChatUser(
                id = "u4",
                handle = "@soundmaker",
                name = "Leo Soundmaker",
                avatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=80",
                bio = "Lo-Fi synthesizer beat architect",
                isOnline = true,
                lastMessage = "Your audio sync on the prism post was fire! 🔥",
                lastMessageTime = "3h ago",
                unreadCount = 0,
                currentTrackTitle = "Velvet Prism Sunset"
            )
        )

        // Prepopulate conversation for @creative_user
        conversationStore["u1"] = mutableListOf(
            ChatMessage(
                id = "m1",
                senderId = "u1",
                senderHandle = "@creative_user",
                text = "Hey there! Welcome to Irispop! ✨",
                timestamp = "10:14 AM",
                isMine = false
            ),
            ChatMessage(
                id = "m2",
                senderId = "me",
                senderHandle = "@irispop_star",
                text = "Love your latest post! The colors are magical 🌈",
                timestamp = "10:16 AM",
                isMine = true
            ),
            ChatMessage(
                id = "m3",
                senderId = "u1",
                senderHandle = "@creative_user",
                text = "Thank you! I used 'Rainbow Frequency' from Spotify for that post. You should use it too!",
                timestamp = "10:18 AM",
                isMine = false,
                attachedTrack = SpotifyCatalog.tracks[0]
            ),
            ChatMessage(
                id = "m4",
                senderId = "u1",
                senderHandle = "@creative_user",
                text = "Hey! Use that Spotify song on your next post, it fits so well! ✨",
                timestamp = "Just now",
                isMine = false
            )
        )

        // Prepopulate conversation for @solaris_vibe
        conversationStore["u2"] = mutableListOf(
            ChatMessage(
                id = "m20",
                senderId = "u2",
                senderHandle = "@solaris_vibe",
                text = "Yo! Check out this fresh Spotify drop for our next collab:",
                timestamp = "Yesterday",
                isMine = false,
                attachedTrack = SpotifyCatalog.tracks[1]
            ),
            ChatMessage(
                id = "m21",
                senderId = "me",
                senderHandle = "@irispop_star",
                text = "This wave is insane! Adding it to my saved songs right now 🎶",
                timestamp = "Yesterday",
                isMine = true
            ),
            ChatMessage(
                id = "m22",
                senderId = "u2",
                senderHandle = "@solaris_vibe",
                text = "Sent you the new soundtrack preview!",
                timestamp = "15m ago",
                isMine = false
            )
        )

        _uiState.update {
            it.copy(
                chatUsers = users,
                totalUnreadChats = users.sumOf { user -> user.unreadCount }
            )
        }
    }

    fun dismissSplash() {
        _uiState.update { it.copy(isSplashVisible = false) }
    }

    fun replaySplash() {
        _uiState.update {
            it.copy(
                isSplashVisible = true,
                isSettingsModalOpen = false
            )
        }
        viewModelScope.launch {
            delay(3200)
            if (_uiState.value.isSplashVisible) {
                _uiState.update { it.copy(isSplashVisible = false) }
            }
        }
    }

    fun setTab(tab: TabType) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun toggleLikePost(postId: String, forceLike: Boolean = false) {
        _uiState.update { current ->
            val updatedPosts = current.posts.map { post ->
                if (post.id == postId) {
                    val willLike = if (forceLike) true else !post.isLiked
                    val newCount = if (willLike) {
                        if (!post.isLiked) post.likesCount + 1 else post.likesCount
                    } else {
                        if (post.isLiked) (post.likesCount - 1).coerceAtLeast(0) else post.likesCount
                    }
                    post.copy(isLiked = willLike, likesCount = newCount)
                } else post
            }
            current.copy(posts = updatedPosts)
        }
    }

    fun toggleSavePost(postId: String) {
        _uiState.update { current ->
            var statusMsg = ""
            val updatedPosts = current.posts.map { post ->
                if (post.id == postId) {
                    val willSave = !post.isSaved
                    statusMsg = if (willSave) "Post saved to bookmarks! 🔖" else "Removed from bookmarks"
                    post.copy(isSaved = willSave)
                } else post
            }
            showToast(statusMsg)
            current.copy(posts = updatedPosts)
        }
    }

    fun cyclePostImage(postId: String, direction: Int) {
        _uiState.update { current ->
            val updatedPosts = current.posts.map { post ->
                if (post.id == postId && post.images.isNotEmpty()) {
                    val nextIndex = (post.currentImageIndex + direction + post.images.size) % post.images.size
                    post.copy(currentImageIndex = nextIndex)
                } else post
            }
            current.copy(posts = updatedPosts)
        }
    }

    fun toggleReelLike(forceLike: Boolean = false) {
        _uiState.update { current ->
            val reel = current.reel
            val willLike = if (forceLike) true else !reel.isLiked
            val countStr = if (willLike) "48.3k" else "48.2k"
            if (willLike) {
                showToast("❤️ Liked this Irispop Reel!")
            }
            current.copy(reel = reel.copy(isLiked = willLike, likesCount = countStr))
        }
    }

    fun toggleFollowReel() {
        _uiState.update { current ->
            val isFollowing = !current.reel.isFollowing
            val msg = if (isFollowing) "Following @solaris_vibe" else "Unfollowed @solaris_vibe"
            showToast(msg)
            current.copy(reel = current.reel.copy(isFollowing = isFollowing))
        }
    }

    fun saveSong(title: String, track: SpotifyTrack? = null) {
        _uiState.update { current ->
            val matchedTrack = track ?: SpotifyCatalog.findByTitle(title)
            if (current.savedSongs.none { it.title.equals(title, ignoreCase = true) }) {
                val newSong = SavedSong(
                    id = "song-${System.currentTimeMillis()}",
                    title = title,
                    duration = matchedTrack.duration,
                    isNew = true,
                    spotifyTrack = matchedTrack
                )
                showToast("🎵 Saved song: \"$title\" to Spotify library!")
                current.copy(savedSongs = listOf(newSong) + current.savedSongs)
            } else {
                showToast("🎵 Song already saved in your library")
                current
            }
        }
    }

    fun addComment(text: String) {
        if (text.isBlank()) return
        val newComment = CommentItem(
            id = "comment-${System.currentTimeMillis()}",
            authorHandle = "@irispop_star",
            authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
            text = text.trim(),
            timeAgo = "Just now",
            likesCount = 0
        )
        _uiState.update { current ->
            current.copy(
                comments = listOf(newComment) + current.comments
            )
        }
        showToast("Comment published!")
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.length > 2) {
            showToast("🔍 Live search filter applied for \"$query\"")
        }
    }

    fun setExploreCategory(category: String) {
        _uiState.update { it.copy(selectedExploreCategory = category) }
    }

    fun setRainbowAuraEnabled(enabled: Boolean) {
        _uiState.update { it.copy(rainbowAuraEnabled = enabled) }
        showToast(if (enabled) "Pastel Neon Aura boost ON ✨" else "Aura boost OFF")
    }

    fun openCreatorSheet() {
        _uiState.update { it.copy(isCreatorSheetOpen = true) }
    }

    fun closeCreatorSheet() {
        _uiState.update { it.copy(isCreatorSheetOpen = false) }
    }

    fun openCommentsSheet() {
        _uiState.update { it.copy(isCommentsSheetOpen = true) }
    }

    fun closeCommentsSheet() {
        _uiState.update { it.copy(isCommentsSheetOpen = false) }
    }

    fun openSettingsModal() {
        _uiState.update { it.copy(isSettingsModalOpen = true) }
    }

    fun closeSettingsModal() {
        _uiState.update { it.copy(isSettingsModalOpen = false) }
    }

    fun openReportModal(userHandle: String) {
        _uiState.update {
            it.copy(
                isReportModalOpen = true,
                reportingUserHandle = userHandle
            )
        }
    }

    fun closeReportModal() {
        _uiState.update { it.copy(isReportModalOpen = false) }
    }

    fun submitReport(reason: String) {
        val user = _uiState.value.reportingUserHandle
        closeReportModal()
        showToast("🚩 Reported $user for \"$reason\". Review underway.")
    }

    fun createContent(type: CreationType) {
        closeCreatorSheet()
        if (type == CreationType.POST) {
            openCreatePostDialog()
        } else {
            showToast("✨ Launched Irispop Studio: Creating ${type.title}!")
        }
    }

    fun sharePost(contentTitle: String = "Irispop Post") {
        showToast("🔗 Link copied to clipboard!")
    }

    // =========================================================================
    // CHAT FEATURE IMPLEMENTATION
    // =========================================================================

    fun openChatList() {
        _uiState.update { it.copy(isChatListOpen = true) }
    }

    fun closeChatList() {
        _uiState.update { it.copy(isChatListOpen = false) }
    }

    fun openChatWithUser(user: ChatUser) {
        // Clear unread count for this user
        val updatedUsers = _uiState.value.chatUsers.map {
            if (it.id == user.id) it.copy(unreadCount = 0) else it
        }
        val msgs = conversationStore[user.id] ?: mutableListOf()
        _uiState.update {
            it.copy(
                chatUsers = updatedUsers,
                totalUnreadChats = updatedUsers.sumOf { u -> u.unreadCount },
                activeChatUser = user,
                activeConversationMessages = msgs.toList(),
                isChatConversationOpen = true,
                isChatListOpen = false,
                isShareToChatOpen = false
            )
        }
    }

    fun openChatWithHandle(handle: String) {
        val targetUser = _uiState.value.chatUsers.firstOrNull {
            it.handle.equals(handle, ignoreCase = true)
        } ?: ChatUser(
            id = "u-${handle.replace("@", "")}",
            handle = handle,
            name = handle.replace("@", "").replaceFirstChar { it.uppercase() },
            avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=120",
            bio = "Irispop community creator",
            isOnline = true,
            lastMessage = "Started a new conversation",
            lastMessageTime = "Just now",
            unreadCount = 0
        ).also { newUser ->
            _uiState.update { it.copy(chatUsers = listOf(newUser) + it.chatUsers) }
        }
        openChatWithUser(targetUser)
    }

    fun closeChatConversation() {
        audioPlayer.stop()
        _uiState.update {
            it.copy(
                isChatConversationOpen = false,
                activeChatUser = null,
                isOtherUserTyping = false,
                isPlayingPreview = false,
                playingTrackId = null
            )
        }
    }

    fun sendChatMessage(text: String, attachedTrack: SpotifyTrack? = null) {
        val currentUser = _uiState.value.activeChatUser ?: return
        if (text.isBlank() && attachedTrack == null) return

        val newMsg = ChatMessage(
            id = "m-${System.currentTimeMillis()}",
            senderId = "me",
            senderHandle = "@irispop_star",
            text = text.trim(),
            timestamp = "Just now",
            isMine = true,
            attachedTrack = attachedTrack
        )

        val list = conversationStore.getOrPut(currentUser.id) { mutableListOf() }
        list.add(newMsg)

        // Update active message list & last message on user card
        val updatedUsers = _uiState.value.chatUsers.map {
            if (it.id == currentUser.id) {
                it.copy(
                    lastMessage = if (attachedTrack != null) "🎵 Shared Spotify Track: ${attachedTrack.title}" else text.trim(),
                    lastMessageTime = "Just now"
                )
            } else it
        }

        _uiState.update {
            it.copy(
                activeConversationMessages = list.toList(),
                chatUsers = updatedUsers
            )
        }

        // Trigger realistic automated response from the friend after a short delay
        viewModelScope.launch {
            delay(1200)
            _uiState.update { it.copy(isOtherUserTyping = true) }
            delay(1500)

            val replyText = when {
                attachedTrack != null -> "Woah, '${attachedTrack.title}' is pure magic! ✨ I'm gonna use this on my next Irispop post!"
                text.contains("song", ignoreCase = true) || text.contains("spotify", ignoreCase = true) || text.contains("track", ignoreCase = true) ->
                    "Spotify sync on Irispop is seamless! You can tap 'Use This Song in Post' to make a post with it right now 🎶"
                text.contains("hi", ignoreCase = true) || text.contains("hey", ignoreCase = true) || text.contains("hello", ignoreCase = true) ->
                    "Hey there! Loving the holographic rainbow aesthetic on your profile! 🌈"
                else -> "Totally agree! Let's create more pastel neon vibes together ✨🌈"
            }

            val replyMsg = ChatMessage(
                id = "m-${System.currentTimeMillis()}",
                senderId = currentUser.id,
                senderHandle = currentUser.handle,
                text = replyText,
                timestamp = "Just now",
                isMine = false
            )

            list.add(replyMsg)

            val updatedUsersAfterReply = _uiState.value.chatUsers.map {
                if (it.id == currentUser.id) {
                    it.copy(
                        lastMessage = replyText,
                        lastMessageTime = "Just now"
                    )
                } else it
            }

            _uiState.update {
                it.copy(
                    activeConversationMessages = list.toList(),
                    chatUsers = updatedUsersAfterReply,
                    isOtherUserTyping = false
                )
            }
        }
    }

    fun reactToMessage(messageId: String, emoji: String) {
        val currentUser = _uiState.value.activeChatUser ?: return
        val list = conversationStore[currentUser.id] ?: return
        val index = list.indexOfFirst { it.id == messageId }
        if (index != -1) {
            val msg = list[index]
            val updated = msg.copy(reaction = if (msg.reaction == emoji) null else emoji)
            list[index] = updated
            _uiState.update {
                it.copy(activeConversationMessages = list.toList())
            }
        }
    }

    fun openSharePostToChat(post: PostItem) {
        _uiState.update {
            it.copy(
                isShareToChatOpen = true,
                postToShareInChat = post
            )
        }
    }

    fun closeSharePostToChat() {
        _uiState.update {
            it.copy(
                isShareToChatOpen = false,
                postToShareInChat = null
            )
        }
    }

    fun sharePostWithUser(post: PostItem, user: ChatUser) {
        val track = post.spotifyTrack ?: SpotifyCatalog.findByTitle(post.songTitle)
        val shareMessage = ChatMessage(
            id = "m-share-${System.currentTimeMillis()}",
            senderId = "me",
            senderHandle = "@irispop_star",
            text = "Check out this Irispop post by ${post.authorHandle}! 🌈 Tap below to use the Spotify song in your post.",
            timestamp = "Just now",
            isMine = true,
            attachedTrack = track,
            attachedPostId = post.id,
            attachedPostImage = post.images.firstOrNull()
        )

        val list = conversationStore.getOrPut(user.id) { mutableListOf() }
        list.add(shareMessage)

        closeSharePostToChat()
        showToast("💌 Sent post with Spotify track to ${user.handle} in chat!")
        openChatWithUser(user)
    }

    // =========================================================================
    // SPOTIFY INTEGRATION & POST CREATION
    // =========================================================================

    fun toggleSpotifyConnection() {
        val willConnect = !_uiState.value.isSpotifyConnected
        _uiState.update {
            it.copy(
                isSpotifyConnected = willConnect,
                spotifyUserName = if (willConnect) "Irispop Resident (Premium)" else "Not Connected"
            )
        }
        showToast(
            if (willConnect) "🟢 Connected to Spotify! Songs & sound library unlocked."
            else "⚪ Disconnected from Spotify"
        )
    }

    fun openSpotifyTrackSheet(track: SpotifyTrack) {
        _uiState.update {
            it.copy(
                isSpotifyTrackSheetOpen = true,
                activeSpotifyTrack = track
            )
        }
    }

    fun closeSpotifyTrackSheet() {
        audioPlayer.stop()
        _uiState.update {
            it.copy(
                isSpotifyTrackSheetOpen = false,
                isPlayingPreview = false,
                playingTrackId = null
            )
        }
    }

    fun toggleAudioPreview(trackId: String) {
        _uiState.update { current ->
            val isCurrentPlaying = current.isPlayingPreview && current.playingTrackId == trackId
            val nextPlaying = !isCurrentPlaying
            if (nextPlaying) {
                audioPlayer.playTrack(trackId)
                showToast("🔊 Playing soundtrack through speaker!")
            } else {
                audioPlayer.stop()
                showToast("⏸️ Paused audio preview")
            }
            current.copy(
                isPlayingPreview = nextPlaying,
                playingTrackId = if (nextPlaying) trackId else null
            )
        }
    }

    fun openSpotifyPicker() {
        _uiState.update { it.copy(isSpotifyPickerOpen = true) }
    }

    fun closeSpotifyPicker() {
        audioPlayer.stop()
        _uiState.update {
            it.copy(
                isSpotifyPickerOpen = false,
                isPlayingPreview = false,
                playingTrackId = null
            )
        }
    }

    fun selectSpotifyTrackForDraft(track: SpotifyTrack) {
        audioPlayer.stop()
        _uiState.update {
            it.copy(
                draftPostSong = track,
                isSpotifyPickerOpen = false,
                isPlayingPreview = false,
                playingTrackId = null
            )
        }
        showToast("🎵 Attached Spotify track: \"${track.title}\"")
    }

    /**
     * Core feature: "use the song on his post"
     * Takes the song from another user's post (or any post), attaches it to a new draft post,
     * and opens the post creator dialog!
     */
    fun useSongFromPost(post: PostItem) {
        val track = post.spotifyTrack ?: SpotifyCatalog.findByTitle(post.songTitle)
        _uiState.update {
            it.copy(
                draftPostSong = track,
                draftPostCaption = "Vibing to '${track.title}' by ${track.artist} ✨ #irispop #spotify",
                isCreatePostDialogOpen = true,
                isSpotifyTrackSheetOpen = false
            )
        }
        showToast("🎧 Attached song \"${track.title}\" from ${post.authorHandle}'s post!")
    }

    fun openCreatePostDialog(preselectedTrack: SpotifyTrack? = null) {
        val initialTrack = preselectedTrack
            ?: _uiState.value.draftPostSong
            ?: SpotifyCatalog.tracks.first()

        _uiState.update {
            it.copy(
                isCreatePostDialogOpen = true,
                draftPostSong = initialTrack,
                draftPostCaption = if (it.draftPostCaption.isBlank()) "New rainbow frequency on Irispop 🌈✨" else it.draftPostCaption
            )
        }
    }

    fun closeCreatePostDialog() {
        _uiState.update { it.copy(isCreatePostDialogOpen = false) }
    }

    fun setDraftCaption(caption: String) {
        _uiState.update { it.copy(draftPostCaption = caption) }
    }

    fun setDraftImage(imageUrl: String) {
        _uiState.update { it.copy(draftPostImageUrl = imageUrl) }
    }

    fun publishPost(caption: String, imageUrl: String, track: SpotifyTrack) {
        val newPost = PostItem(
            id = "post-${System.currentTimeMillis()}",
            authorHandle = "@irispop_star",
            authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300",
            images = listOf(imageUrl),
            currentImageIndex = 0,
            caption = caption.ifBlank { "Sharing our holographic vibes with Spotify sound ✨ #irispop" },
            songTitle = track.title,
            spotifyTrack = track,
            likesCount = 1,
            isLiked = true,
            isSaved = false,
            commentsCount = 0,
            timeAgo = "Just now"
        )

        // Also add to saved songs if not already present
        saveSong(track.title, track)

        _uiState.update { current ->
            current.copy(
                posts = listOf(newPost) + current.posts,
                isCreatePostDialogOpen = false,
                activeTab = TabType.FEED,
                draftPostCaption = ""
            )
        }
        showToast("🎉 Post published with Spotify track \"${track.title}\"!")
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(toastMessage = message) }
            delay(2600)
            _uiState.update {
                if (it.toastMessage == message) it.copy(toastMessage = null) else it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
    }
}
