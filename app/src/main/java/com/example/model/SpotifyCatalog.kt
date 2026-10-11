package com.example.model

// Spotify track helper definitions for default catalog
object SpotifyCatalog {
    val tracks = listOf(
        SpotifyTrack(
            id = "sp-1",
            title = "Rainbow Frequency (Original Mix)",
            artist = "Solaris & Iris Pop",
            album = "Prism Glow Vol. 1",
            albumArt = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400",
            duration = "3:24",
            spotifyUri = "spotify:track:4cOdK2wGLETKBW3PvgPWqT",
            previewAudioHint = "Holographic Synthwave",
            isPopular = true
        ),
        SpotifyTrack(
            id = "sp-2",
            title = "Luminescence Wave 07",
            artist = "Solaris Vibe",
            album = "Chromatic Shift",
            albumArt = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=400",
            duration = "2:51",
            spotifyUri = "spotify:track:7wGoVu4DEXVFDhu0qmKyW6",
            previewAudioHint = "Pastel Chillstep",
            isPopular = true
        ),
        SpotifyTrack(
            id = "sp-3",
            title = "Midnight Iridescence",
            artist = "Aurora Vibes",
            album = "Neon Twilight",
            albumArt = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=400",
            duration = "3:10",
            spotifyUri = "spotify:track:11dFghVXANMlKmJXsNCbNl",
            previewAudioHint = "Dream Pop Euphoria",
            isPopular = true
        ),
        SpotifyTrack(
            id = "sp-4",
            title = "Velvet Prism Sunset",
            artist = "Soundmaker",
            album = "Velvet Horizons",
            albumArt = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=400",
            duration = "2:38",
            spotifyUri = "spotify:track:0VjIjW4GlUZAMYd2vXMi3b",
            previewAudioHint = "Lo-Fi Rainbow Melodies",
            isPopular = false
        ),
        SpotifyTrack(
            id = "sp-5",
            title = "Starfall Glitch",
            artist = "Kai & Neona",
            album = "Future Spectrum",
            albumArt = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=400",
            duration = "3:45",
            spotifyUri = "spotify:track:2XWjPtKtf2UCy70kKkXbsp",
            previewAudioHint = "Future Bass Prism",
            isPopular = true
        ),
        SpotifyTrack(
            id = "sp-6",
            title = "Hyper-Pastel Resonance",
            artist = "Creative Mind",
            album = "Studio Iris",
            albumArt = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=400",
            duration = "2:15",
            spotifyUri = "spotify:track:3n3Ppam7vgaVa1iaRUc9Lp",
            previewAudioHint = "Ambient Pastel Glow",
            isPopular = false
        )
    )

    fun findByTitle(title: String): SpotifyTrack {
        return tracks.firstOrNull { it.title.equals(title, ignoreCase = true) || title.contains(it.title, ignoreCase = true) }
            ?: SpotifyTrack(
                id = "custom-${title.hashCode()}",
                title = title,
                artist = "Featured Artist",
                album = "Irispop Soundtrack",
                albumArt = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400",
                duration = "3:00",
                spotifyUri = "spotify:track:custom",
                previewAudioHint = "Spotify Verified Soundtrack"
            )
    }
}
