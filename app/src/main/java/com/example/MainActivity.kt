package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.SpotifyCatalog
import com.example.model.TabType
import com.example.ui.components.ChatConversationSheet
import com.example.ui.components.ChatListSheet
import com.example.ui.components.CommentsSheet
import com.example.ui.components.CreatePostDialog
import com.example.ui.components.CreatorSheet
import com.example.ui.components.IrispopBottomBar
import com.example.ui.components.IrispopTopBar
import com.example.ui.components.ReportDialog
import com.example.ui.components.SettingsModal
import com.example.ui.components.SharePostToChatSheet
import com.example.ui.components.SplashScreen
import com.example.ui.components.SpotifyMusicPickerSheet
import com.example.ui.components.SpotifyTrackDetailSheet
import com.example.ui.components.ToastBanner
import com.example.ui.screens.FeedScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReelsScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.IrisBgDark
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.IrispopViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: IrispopViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                IrispopApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun IrispopApp(viewModel: IrispopViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle back button when not on FEED tab
    if (uiState.activeTab != TabType.FEED) {
        BackHandler {
            viewModel.setTab(TabType.FEED)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(IrisBgDark)
            .testTag("irispop_main_container")
    ) {
        Scaffold(
            topBar = {
                IrispopTopBar(
                    unreadChatCount = uiState.totalUnreadChats,
                    isSpotifyConnected = uiState.isSpotifyConnected,
                    onReplaySplash = { viewModel.replaySplash() },
                    onTitleClick = { viewModel.setTab(TabType.FEED) },
                    onChatClick = { viewModel.openChatList() },
                    onSpotifyClick = { viewModel.openSpotifyPicker() },
                    onSettingsClick = { viewModel.openSettingsModal() }
                )
            },
            bottomBar = {
                IrispopBottomBar(
                    activeTab = uiState.activeTab,
                    onTabSelected = { viewModel.setTab(it) },
                    onPlusClicked = { viewModel.openCreatorSheet() }
                )
            },
            containerColor = IrisBgDark,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (uiState.activeTab) {
                    TabType.FEED -> {
                        FeedScreen(
                            stories = uiState.stories,
                            posts = uiState.posts,
                            auraEnabled = uiState.rainbowAuraEnabled,
                            onOpenStoryCreator = { viewModel.openCreatorSheet() },
                            onStoryClick = { story ->
                                viewModel.showToast("Viewing story of @${story.name}")
                            },
                            onLikePost = { postId -> viewModel.toggleLikePost(postId) },
                            onCommentPost = { viewModel.openCommentsSheet() },
                            onSendPost = { post -> viewModel.openSharePostToChat(post) },
                            onSavePost = { postId -> viewModel.toggleSavePost(postId) },
                            onPrevImage = { postId -> viewModel.cyclePostImage(postId, -1) },
                            onNextImage = { postId -> viewModel.cyclePostImage(postId, 1) },
                            onSaveSong = { songTitle -> viewModel.saveSong(songTitle) },
                            isPlayingPreview = uiState.isPlayingPreview,
                            playingTrackId = uiState.playingTrackId,
                            onTogglePlayAudio = { trackId -> viewModel.toggleAudioPreview(trackId) },
                            onUseSongInPost = { post -> viewModel.useSongFromPost(post) },
                            onOpenSpotifyTrack = { track -> viewModel.openSpotifyTrackSheet(track) },
                            onChatWithAuthor = { handle -> viewModel.openChatWithHandle(handle) },
                            onReportPost = { handle -> viewModel.openReportModal(handle) }
                        )
                    }

                    TabType.REELS -> {
                        ReelsScreen(
                            reel = uiState.reel,
                            onLikeClick = { viewModel.toggleReelLike() },
                            onCommentClick = { viewModel.openCommentsSheet() },
                            onSaveSong = { songTitle -> viewModel.saveSong(songTitle) },
                            onShareClick = { viewModel.sharePost() },
                            onReportClick = { handle -> viewModel.openReportModal(handle) },
                            onToggleFollow = { viewModel.toggleFollowReel() }
                        )
                    }

                    TabType.SEARCH -> {
                        SearchScreen(
                            searchQuery = uiState.searchQuery,
                            onSearchQueryChange = { viewModel.setSearchQuery(it) },
                            selectedCategory = uiState.selectedExploreCategory,
                            onCategorySelected = { viewModel.setExploreCategory(it) },
                            exploreItems = uiState.exploreItems,
                            onItemClick = { item ->
                                viewModel.showToast("Selected ${item.titleOrTag}")
                            }
                        )
                    }

                    TabType.PROFILE -> {
                        ProfileScreen(
                            savedSongs = uiState.savedSongs,
                            isSpotifyConnected = uiState.isSpotifyConnected,
                            spotifyUsername = uiState.spotifyUserName,
                            onEditProfileClick = { viewModel.showToast("Profile edit modal opened") },
                            onShareProfileClick = { viewModel.sharePost() },
                            onOpenChatsClick = { viewModel.openChatList() },
                            onManageSpotifyClick = { viewModel.openSpotifyPicker() },
                            onUseSavedSongInPost = { songTitle ->
                                val track = SpotifyCatalog.findByTitle(songTitle)
                                viewModel.openCreatePostDialog(track)
                            }
                        )
                    }
                }
            }
        }

        // Floating Toast Banner
        ToastBanner(
            message = uiState.toastMessage,
            modifier = Modifier.padding(top = 48.dp)
        )

        // Creator Studio Sheet (+)
        CreatorSheet(
            isOpen = uiState.isCreatorSheetOpen,
            onDismiss = { viewModel.closeCreatorSheet() },
            onSelectType = { type -> viewModel.createContent(type) }
        )

        // Create Post Dialog with Spotify Soundtrack
        CreatePostDialog(
            isOpen = uiState.isCreatePostDialogOpen,
            initialCaption = uiState.draftPostCaption,
            initialSong = uiState.draftPostSong,
            initialImageUrl = uiState.draftPostImageUrl,
            onDismiss = { viewModel.closeCreatePostDialog() },
            onOpenSpotifyPicker = { viewModel.openSpotifyPicker() },
            onPublish = { caption, imageUrl, song ->
                viewModel.publishPost(caption, imageUrl, song)
            }
        )

        // Chat Inbox Sheet (List of conversations)
        ChatListSheet(
            isOpen = uiState.isChatListOpen,
            chatUsers = uiState.chatUsers,
            onDismiss = { viewModel.closeChatList() },
            onSelectUser = { user -> viewModel.openChatWithUser(user) },
            onOpenSpotifyCatalog = { viewModel.openSpotifyPicker() }
        )

        // 1-on-1 Chat Conversation Sheet
        ChatConversationSheet(
            isOpen = uiState.isChatConversationOpen,
            user = uiState.activeChatUser,
            messages = uiState.activeConversationMessages,
            isOtherUserTyping = uiState.isOtherUserTyping,
            isPlaying = uiState.isPlayingPreview,
            playingTrackId = uiState.playingTrackId,
            onDismiss = { viewModel.closeChatConversation() },
            onSendMessage = { text, track -> viewModel.sendChatMessage(text, track) },
            onReactToMessage = { msgId, emoji -> viewModel.reactToMessage(msgId, emoji) },
            onTogglePlayAudio = { trackId -> viewModel.toggleAudioPreview(trackId) },
            onUseSongInPost = { track -> viewModel.openCreatePostDialog(track) },
            onOpenSpotifyPicker = { viewModel.openSpotifyPicker() }
        )

        // Share Post & Spotify track to Chat Sheet
        SharePostToChatSheet(
            isOpen = uiState.isShareToChatOpen,
            post = uiState.postToShareInChat,
            users = uiState.chatUsers,
            onDismiss = { viewModel.closeSharePostToChat() },
            onSendToUser = { post, user -> viewModel.sharePostWithUser(post, user) }
        )

        // Spotify Track Details & Player Sheet
        SpotifyTrackDetailSheet(
            isOpen = uiState.isSpotifyTrackSheetOpen,
            track = uiState.activeSpotifyTrack,
            isPlaying = uiState.isPlayingPreview,
            onDismiss = { viewModel.closeSpotifyTrackSheet() },
            onTogglePlay = { trackId -> viewModel.toggleAudioPreview(trackId) },
            onUseSongInPost = { track -> viewModel.openCreatePostDialog(track) },
            onSendInChat = { track -> viewModel.openChatList() },
            onSaveSong = { track -> viewModel.saveSong(track.title, track) }
        )

        // Spotify Music Picker Sheet
        SpotifyMusicPickerSheet(
            isOpen = uiState.isSpotifyPickerOpen,
            tracks = uiState.spotifyTracks,
            selectedTrackId = uiState.draftPostSong?.id,
            isPlaying = uiState.isPlayingPreview,
            playingTrackId = uiState.playingTrackId,
            onDismiss = { viewModel.closeSpotifyPicker() },
            onTogglePlay = { trackId -> viewModel.toggleAudioPreview(trackId) },
            onSelectTrack = { track -> viewModel.selectSpotifyTrackForDraft(track) }
        )

        // Comments Bottom Sheet
        CommentsSheet(
            isOpen = uiState.isCommentsSheetOpen,
            comments = uiState.comments,
            onDismiss = { viewModel.closeCommentsSheet() },
            onAddComment = { text -> viewModel.addComment(text) }
        )

        // Settings Modal (3-dots)
        SettingsModal(
            isOpen = uiState.isSettingsModalOpen,
            rainbowAuraEnabled = uiState.rainbowAuraEnabled,
            onToggleRainbowAura = { viewModel.setRainbowAuraEnabled(it) },
            onReplaySplash = { viewModel.replaySplash() },
            onDismiss = { viewModel.closeSettingsModal() }
        )

        // Report Dialog
        ReportDialog(
            isOpen = uiState.isReportModalOpen,
            userHandle = uiState.reportingUserHandle,
            onDismiss = { viewModel.closeReportModal() },
            onSubmitReport = { reason -> viewModel.submitReport(reason) }
        )

        // Converging 4-part Splash Screen Overlay
        SplashScreen(
            visible = uiState.isSplashVisible,
            onDismiss = { viewModel.dismissSplash() }
        )
    }
}
