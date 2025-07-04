package com.eugene.testtaskfortechspire.di.modules

import com.eugene.data.repository.PointsRepositoryImpl
import com.eugene.domain.repository.PointsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindPointsRepository(
        impl: PointsRepositoryImpl
    ): PointsRepository
}