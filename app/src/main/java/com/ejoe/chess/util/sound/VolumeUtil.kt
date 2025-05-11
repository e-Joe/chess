package com.ejoe.chess.util.sound

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
import android.media.MediaPlayer
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.math.min

fun fadeVolume(
    player: MediaPlayer?,
    from: Float,
    to: Float,
    duration: Long = 500L,
    onEnd: (() -> Unit)? = null
) {
    player ?: return

    CoroutineScope(Dispatchers.Main).launch {
        val steps = 20
        val stepDuration = duration / steps
        val delta = (to - from) / steps

        for (i in 0..steps) {
            val vol = min(1f, max(0f, from + i * delta))
            player.setVolume(vol, vol)
            delay(stepDuration)
        }
        onEnd?.invoke()
    }
}