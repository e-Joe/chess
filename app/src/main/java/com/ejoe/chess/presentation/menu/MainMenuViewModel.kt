package com.ejoe.chess.presentation.menu

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejoe.chess.R
import kotlinx.coroutines.*

/**
 * ViewModel responsible for managing main menu.
 *
 * Created by Ilija Vucetic on 11.5.25.
 *
 * Responsibilities:
 * - Controls background music playback using [MediaPlayer]
 * - Handles smooth volume transitions (fade-in/fade-out)
 * - Automatically releases resources on lifecycle cleanup
 */
class MainMenuViewModel : ViewModel() {

    //#region MediaPlayer Management

    // Android media player instance
    private var mediaPlayer: MediaPlayer? = null

    // Coroutine job for fading volume
    private var fadeJob: Job? = null

    //#endregion

    //#region Public Methods

    /**
     * Starts playing the background music with a fade-in effect.
     *
     * @param context Required to access the media file resource.
     */
    fun startMusic(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.game_loop).apply {
                isLooping = true
                setVolume(0f, 0f) // Start silent
                start()
                fadeToVolume(1f) // Fade to full volume
            }
        } else {
            mediaPlayer?.start()
            fadeToVolume(1f)
        }
    }

    /**
     * Pauses the music with a fade-out effect.
     */
    fun pauseMusic() {
        fadeToVolume(0f) {
            mediaPlayer?.pause()
        }
    }

    //#endregion

    //#region Volume Fading

    /**
     * Smoothly transitions the volume of the music.
     *
     * @param target The target volume level (0.0 to 1.0).
     * @param onEnd Optional callback invoked after fading completes.
     */
    private fun fadeToVolume(target: Float, onEnd: (() -> Unit)? = null) {
        fadeJob?.cancel()
        fadeJob = viewModelScope.launch(Dispatchers.Main) {
            val steps = 20
            val duration = 500L // in milliseconds
            val stepDuration = duration / steps
            val from = mediaPlayer?.volumeOrZero() ?: return@launch
            val delta = (target - from) / steps

            for (i in 0..steps) {
                val vol = (from + i * delta).coerceIn(0f, 1f)
                try {
                    mediaPlayer?.setVolume(vol, vol)
                } catch (e: IllegalStateException) {
                    break // MediaPlayer might be released or in invalid state
                }
                delay(stepDuration)
            }
            onEnd?.invoke()
        }
    }

    /**
     * Returns the current volume of the MediaPlayer or 0 if unavailable.
     */
    private fun MediaPlayer.volumeOrZero(): Float {
        return try {
            if (isPlaying || isLooping) 1f else 0f
        } catch (e: Exception) {
            0f
        }
    }

    //#endregion

    //#region Lifecycle Cleanup

    /**
     * Releases the MediaPlayer and cancels any ongoing fade operations.
     */
    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    //#endregion
}