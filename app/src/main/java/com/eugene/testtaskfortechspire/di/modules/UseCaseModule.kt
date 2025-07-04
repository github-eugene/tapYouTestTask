package com.eugene.testtaskfortechspire.di.modules

import android.content.Context
import com.eugene.domain.repository.PointsRepository
import com.eugene.domain.usecase.GetPointsUseCase
import com.eugene.testtaskfortechspire.usecase.SaveGraphUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object UseCaseModule {

    @Provides
    fun provideGetPointsUseCase(
        repository: PointsRepository
    ): GetPointsUseCase {
        return GetPointsUseCase(repository)
    }

    @Provides
    fun provideSaveGraphUseCase(context: Context): SaveGraphUseCase {
        return SaveGraphUseCase(context)
    }
}