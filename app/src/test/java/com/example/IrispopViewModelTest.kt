package com.example

import com.example.model.CreationType
import com.example.model.SpotifyCatalog
import com.example.model.TabType
import com.example.viewmodel.IrispopViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IrispopViewModelTest {

    private lateinit var viewModel: IrispopViewModel

    @Before
    fun setUp() {
        viewModel = IrispopViewModel()
    }

    @Test
    fun testTabNavigation() {
        assertEquals(TabType.FEED, viewModel.uiState.value.activeTab)
        viewModel.setTab(TabType.REELS)
        assertEquals(TabType.REELS, viewModel.uiState.value.activeTab)
        viewModel.setTab(TabType.PROFILE)
        assertEquals(TabType.PROFILE, viewModel.uiState.value.activeTab)
    }

    @Test
    fun testToggleLikePost() {
        val initialPost = viewModel.uiState.value.posts.first()
        val initialCount = initialPost.likesCount
        assertFalse(initialPost.isLiked)

        viewModel.toggleLikePost(initialPost.id)
        val likedPost = viewModel.uiState.value.posts.first()
        assertTrue(likedPost.isLiked)
        assertEquals(initialCount + 1, likedPost.likesCount)

        viewModel.toggleLikePost(initialPost.id)
        val unlikedPost = viewModel.uiState.value.posts.first()
        assertFalse(unlikedPost.isLiked)
        assertEquals(initialCount, unlikedPost.likesCount)
    }

    @Test
    fun testSaveSongToProfileLibrary() {
        val initialCount = viewModel.uiState.value.savedSongs.size
        viewModel.saveSong("Aurora Prism Sunset Theme")
        val newCount = viewModel.uiState.value.savedSongs.size
        assertEquals(initialCount + 1, newCount)
        assertEquals("Aurora Prism Sunset Theme", viewModel.uiState.value.savedSongs.first().title)

        // Duplicate prevention test
        viewModel.saveSong("Aurora Prism Sunset Theme")
        assertEquals(newCount, viewModel.uiState.value.savedSongs.size)
    }

    @Test
    fun testAddComment() {
        val initialCount = viewModel.uiState.value.comments.size
        viewModel.addComment("Loving the Irispop holographic vibe! ✨")
        val newCount = viewModel.uiState.value.comments.size
        assertEquals(initialCount + 1, newCount)
        assertEquals("Loving the Irispop holographic vibe! ✨", viewModel.uiState.value.comments.first().text)
    }

    @Test
    fun testAuraBoostToggle() {
        assertTrue(viewModel.uiState.value.rainbowAuraEnabled)
        viewModel.setRainbowAuraEnabled(false)
        assertFalse(viewModel.uiState.value.rainbowAuraEnabled)
        viewModel.setRainbowAuraEnabled(true)
        assertTrue(viewModel.uiState.value.rainbowAuraEnabled)
    }

    @Test
    fun testReelLikeAndFollow() {
        val initialReel = viewModel.uiState.value.reel
        assertFalse(initialReel.isLiked)
        assertFalse(initialReel.isFollowing)

        viewModel.toggleReelLike()
        assertTrue(viewModel.uiState.value.reel.isLiked)

        viewModel.toggleFollowReel()
        assertTrue(viewModel.uiState.value.reel.isFollowing)
    }

    @Test
    fun testChatFeatureFlow() {
        // Test chat list open & close
        assertFalse(viewModel.uiState.value.isChatListOpen)
        viewModel.openChatList()
        assertTrue(viewModel.uiState.value.isChatListOpen)
        viewModel.closeChatList()
        assertFalse(viewModel.uiState.value.isChatListOpen)

        // Test opening conversation with user
        val targetUser = viewModel.uiState.value.chatUsers.first()
        viewModel.openChatWithUser(targetUser)
        assertTrue(viewModel.uiState.value.isChatConversationOpen)
        assertEquals(targetUser.id, viewModel.uiState.value.activeChatUser?.id)

        // Test sending message in chat
        val beforeCount = viewModel.uiState.value.activeConversationMessages.size
        viewModel.sendChatMessage("Hello from test! Loving the soundtrack 🎶")
        val afterCount = viewModel.uiState.value.activeConversationMessages.size
        assertEquals(beforeCount + 1, afterCount)
        assertEquals("Hello from test! Loving the soundtrack 🎶", viewModel.uiState.value.activeConversationMessages.last().text)

        // Test message reaction
        val lastMsgId = viewModel.uiState.value.activeConversationMessages.last().id
        viewModel.reactToMessage(lastMsgId, "✨")
        assertEquals("✨", viewModel.uiState.value.activeConversationMessages.last().reaction)
    }

    @Test
    fun testSpotifyIntegrationAndUseSongFromPost() {
        // Test Spotify connection toggle
        val initialConnected = viewModel.uiState.value.isSpotifyConnected
        viewModel.toggleSpotifyConnection()
        assertEquals(!initialConnected, viewModel.uiState.value.isSpotifyConnected)
        viewModel.toggleSpotifyConnection()
        assertEquals(initialConnected, viewModel.uiState.value.isSpotifyConnected)

        // Test Use Song From Post
        val post = viewModel.uiState.value.posts.first()
        viewModel.useSongFromPost(post)
        assertTrue(viewModel.uiState.value.isCreatePostDialogOpen)
        assertNotNull(viewModel.uiState.value.draftPostSong)
        assertEquals(post.songTitle, viewModel.uiState.value.draftPostSong?.title)

        // Test publishing new post with the Spotify soundtrack
        val initialPostsCount = viewModel.uiState.value.posts.size
        val songToUse = viewModel.uiState.value.draftPostSong!!
        viewModel.publishPost("My new post with Spotify track! 🌈", "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800", songToUse)

        assertEquals(initialPostsCount + 1, viewModel.uiState.value.posts.size)
        val newestPost = viewModel.uiState.value.posts.first()
        assertEquals(songToUse.title, newestPost.songTitle)
        assertEquals(songToUse, newestPost.spotifyTrack)
        assertFalse(viewModel.uiState.value.isCreatePostDialogOpen)
    }
}
