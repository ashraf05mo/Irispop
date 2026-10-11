package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/**
 * Real Audio Engine for Irispop
 * Synthesizes and plays authentic melodious soundtracks directly through the device speaker
 * using Android's native AudioTrack PCM streaming.
 */
class IrispopAudioPlayer {

    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    data class Note(
        val freq: Double,
        val durationMs: Int = 320,
        val waveType: Int = 0 // 0 = warm synth, 1 = dreamy bell, 2 = ambient pad
    )

    fun playTrack(trackId: String) {
        stop()
        playbackJob = scope.launch {
            try {
                val sampleRate = 44100
                val minBufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                ).coerceAtLeast(sampleRate / 4)

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(minBufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack = track
                track.play()

                val melody = getMelodyForTrack(trackId)
                var noteIdx = 0

                while (isActive) {
                    val note = melody[noteIdx % melody.size]
                    noteIdx++

                    val buffer = generateTone(
                        frequency = note.freq,
                        durationMs = note.durationMs,
                        sampleRate = sampleRate,
                        waveType = note.waveType
                    )

                    if (!isActive) break

                    val written = track.write(buffer, 0, buffer.size)
                    if (written < 0) break
                }
            } catch (e: Throwable) {
                Log.w("IrispopAudioPlayer", "AudioTrack playback notice: ${e.message}")
            }
        }
    }

    fun stop() {
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.let {
                if (it.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    it.stop()
                }
                it.release()
            }
        } catch (e: Throwable) {
            // Ignore during teardown
        }
        audioTrack = null
    }

    private fun getMelodyForTrack(trackId: String): List<Note> {
        return when {
            trackId.contains("sp-1") || trackId.contains("rainbow", ignoreCase = true) -> {
                // "Rainbow Frequency" - Bright holographic pentatonic arpeggio
                listOf(
                    Note(440.0, 240, 0),  // A4
                    Note(554.37, 240, 0), // C#5
                    Note(659.25, 240, 0), // E5
                    Note(880.0, 360, 1),  // A5
                    Note(739.99, 240, 0), // F#5
                    Note(659.25, 240, 0), // E5
                    Note(554.37, 360, 0), // C#5
                    Note(440.0, 420, 1)   // A4
                )
            }
            trackId.contains("sp-2") || trackId.contains("luminescence", ignoreCase = true) -> {
                // "Luminescence Wave 07" - Smooth chillwave chords & descending progression
                listOf(
                    Note(349.23, 300, 2), // F4
                    Note(440.0, 300, 0),  // A4
                    Note(523.25, 300, 0), // C5
                    Note(659.25, 450, 1), // E5
                    Note(587.33, 300, 0), // D5
                    Note(493.88, 300, 0), // B4
                    Note(392.00, 450, 2)  // G4
                )
            }
            trackId.contains("sp-3") || trackId.contains("midnight", ignoreCase = true) -> {
                // "Midnight Iridescence" - Dreamy twilight melody
                listOf(
                    Note(293.66, 280, 1), // D4
                    Note(369.99, 280, 1), // F#4
                    Note(440.0, 280, 1),  // A4
                    Note(493.88, 350, 1), // B4
                    Note(587.33, 500, 2), // D5
                    Note(440.0, 300, 1)   // A4
                )
            }
            trackId.contains("sp-4") || trackId.contains("velvet", ignoreCase = true) -> {
                // "Velvet Prism Sunset" - Warm Lo-Fi melody
                listOf(
                    Note(329.63, 320, 0), // E4
                    Note(392.00, 320, 0), // G4
                    Note(493.88, 320, 0), // B4
                    Note(587.33, 480, 1), // D5
                    Note(523.25, 320, 0), // C5
                    Note(392.00, 400, 0)  // G4
                )
            }
            trackId.contains("sp-5") || trackId.contains("starfall", ignoreCase = true) -> {
                // "Starfall Glitch" - Future prism rhythmic lead
                listOf(
                    Note(523.25, 200, 1), // C5
                    Note(659.25, 200, 1), // E5
                    Note(783.99, 200, 1), // G5
                    Note(1046.50, 350, 1),// C6
                    Note(880.00, 200, 0), // A5
                    Note(659.25, 300, 0)  // E5
                )
            }
            else -> {
                // "Hyper-Pastel Resonance" & Custom tracks - Ambient harmonic sequence
                listOf(
                    Note(261.63, 350, 2), // C4
                    Note(329.63, 350, 2), // E4
                    Note(392.00, 350, 2), // G4
                    Note(523.25, 500, 1), // C5
                    Note(440.00, 350, 2), // A4
                    Note(329.63, 400, 2)  // E4
                )
            }
        }
    }

    /**
     * Synthesizes 16-bit PCM samples with smooth attack, sustain, and release envelopes
     * and subtle harmonic overtones so it produces rich, musical sounds on the device speaker.
     */
    private fun generateTone(
        frequency: Double,
        durationMs: Int,
        sampleRate: Int,
        waveType: Int
    ): ShortArray {
        val totalSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(100)
        val buffer = ShortArray(totalSamples)

        val attackSamples = (totalSamples * 0.12).toInt().coerceAtLeast(1)
        val releaseSamples = (totalSamples * 0.25).toInt().coerceAtLeast(1)
        val sustainSamples = totalSamples - attackSamples - releaseSamples

        val angularFreq = 2.0 * PI * frequency / sampleRate

        for (i in 0 until totalSamples) {
            // Envelope calculation (ADSR)
            val envelope = when {
                i < attackSamples -> i.toDouble() / attackSamples
                i < attackSamples + sustainSamples -> 1.0
                else -> {
                    val releaseIdx = i - (attackSamples + sustainSamples)
                    (1.0 - (releaseIdx.toDouble() / releaseSamples)).coerceAtLeast(0.0)
                }
            }

            // Synthesize fundamental + harmonic overtones
            val sampleValue = when (waveType) {
                1 -> {
                    // Dreamy bell/chime: fundamental + 2nd + 3rd harmonic with decay
                    val s1 = sin(angularFreq * i)
                    val s2 = 0.45 * sin(angularFreq * 2.0 * i)
                    val s3 = 0.20 * sin(angularFreq * 3.0 * i)
                    (s1 + s2 + s3) / 1.65
                }
                2 -> {
                    // Ambient warm pad: soft sine with octave sub
                    val s1 = sin(angularFreq * i)
                    val s2 = 0.35 * sin(angularFreq * 0.5 * i)
                    val s3 = 0.25 * sin(angularFreq * 2.0 * i)
                    (s1 + s2 + s3) / 1.6
                }
                else -> {
                    // Warm synthwave lead: fundamental + 2nd harmonic
                    val s1 = sin(angularFreq * i)
                    val s2 = 0.35 * sin(angularFreq * 2.0 * i)
                    (s1 + s2) / 1.35
                }
            }

            // Safe volume scaling to prevent speaker clipping
            val maxAmp = 11000.0
            val pcm = (sampleValue * envelope * maxAmp).coerceIn(-32767.0, 32767.0).toInt().toShort()
            buffer[i] = pcm
        }

        return buffer
    }
}
