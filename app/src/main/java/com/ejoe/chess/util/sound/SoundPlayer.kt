package com.ejoe.chess.util.sound

import android.content.Context
import android.media.MediaPlayer

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
object SoundPlayer {

    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, soundResId: Int) {
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(context, soundResId).apply {
            setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
            start()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}