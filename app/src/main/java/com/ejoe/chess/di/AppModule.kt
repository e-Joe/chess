package com.ejoe.chess.di

import androidx.room.Room
import com.ejoe.chess.data.local.AppDatabase
import com.ejoe.chess.data.repository.GameRepositoryImpl
import com.ejoe.chess.domain.repository.GameRepository
import com.ejoe.chess.domain.usecase.GetLeaderboardUseCase
import com.ejoe.chess.domain.usecase.SaveGameResultUseCase
import com.ejoe.chess.presentation.game.GameViewModel
import com.ejoe.chess.presentation.leaderboard.LeaderboardViewModel
import com.ejoe.chess.presentation.menu.MainMenuViewModel
import com.ejoe.chess.presentation.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Ilija Vucetic on 11.5.25.
 */

val appModule = module {

    // Room Database
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "chesstry_db"
        ).fallbackToDestructiveMigration(false).build()
    }

    single { get<AppDatabase>().gameResultDao() }

    single<GameRepository> { GameRepositoryImpl(get()) }

    factory { SaveGameResultUseCase(get()) }
    factory { GetLeaderboardUseCase(get()) }

    viewModel { SplashViewModel() }
    viewModel { MainMenuViewModel() }
    viewModel {
        GameViewModel(
            getGameSettingsUseCase = get(),
            saveGameResultUseCase = get()
        )
    }
    viewModel { LeaderboardViewModel(get()) }
}