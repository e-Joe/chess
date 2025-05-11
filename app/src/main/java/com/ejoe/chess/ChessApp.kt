package com.ejoe.chess

import android.app.Application
import com.ejoe.chess.di.appModule
import com.ejoe.chess.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by Ilija Vucetic on 11.5.25.
 */
class ChessApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ChessApp)
            modules(
                appModule,
                settingsModule
            )
        }
    }
}