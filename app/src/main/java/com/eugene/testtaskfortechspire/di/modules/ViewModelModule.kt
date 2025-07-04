package com.eugene.testtaskfortechspire.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eugene.domain.usecase.GetPointsUseCase
import com.eugene.testtaskfortechspire.di.factory.DaggerViewModelFactory
import com.eugene.testtaskfortechspire.ui.fragments.graph.GraphViewModel
import com.eugene.testtaskfortechspire.ui.fragments.main.MainViewModel
import com.eugene.testtaskfortechspire.usecase.SaveGraphUseCase
import dagger.Module
import dagger.Provides
import dagger.internal.Provider

@Module
object ViewModelModule {

    @Provides
    fun provideMainViewModel(getPointsUseCase: GetPointsUseCase): MainViewModel {
        return MainViewModel(getPointsUseCase)
    }

    @Provides
    fun provideGraphViewModel(saveGraphUseCase: SaveGraphUseCase): GraphViewModel {
        return GraphViewModel(saveGraphUseCase)
    }

    @Provides
    fun provideViewModels(
        mainViewModel: MainViewModel,
        graphViewModel: GraphViewModel
    ): Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>> {
        return mapOf(
            MainViewModel::class.java to Provider { mainViewModel },
            GraphViewModel::class.java to Provider { graphViewModel }
        )
    }

    @Provides
    fun provideViewModelFactory(
        creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory {
        return DaggerViewModelFactory(creators)
    }
}