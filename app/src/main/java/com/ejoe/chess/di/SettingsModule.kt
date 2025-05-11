package com.ejoe.chess.di

import android.content.Context
import android.content.SharedPreferences
import com.ejoe.chess.data.local.sharerpreference.SettingsStorageImpl
import com.ejoe.chess.data.repository.SettingsRepositoryImpl
import com.ejoe.chess.domain.repository.SettingsRepository
import com.ejoe.chess.domain.storage.SettingsStorage
import com.ejoe.chess.domain.usecase.GetGameSettingsUseCase
import com.ejoe.chess.domain.usecase.SaveGameSettingsUseCase
import com.ejoe.chess.presentation.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Ilija Vucetic on 11.5.25.
 */

val settingsModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences("chess_settings", Context.MODE_PRIVATE)
    }
    single<SettingsStorage> { SettingsStorageImpl(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    factory { GetGameSettingsUseCase(get()) }
    factory { SaveGameSettingsUseCase(get()) }

    viewModel {
        SettingsViewModel(
            getGameSettingsUseCase = get(),
            saveGameSettingsUseCase = get()
        )
    }
}