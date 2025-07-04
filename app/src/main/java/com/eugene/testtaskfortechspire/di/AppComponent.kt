package com.eugene.testtaskfortechspire.di

import androidx.lifecycle.ViewModelProvider
import com.eugene.testtaskfortechspire.di.modules.AppModule
import com.eugene.testtaskfortechspire.di.modules.NetworkModule
import com.eugene.testtaskfortechspire.di.modules.RepositoryModule
import com.eugene.testtaskfortechspire.di.modules.UseCaseModule
import com.eugene.testtaskfortechspire.di.modules.ViewModelModule
import com.eugene.testtaskfortechspire.ui.fragments.graph.GraphFragment
import com.eugene.testtaskfortechspire.ui.fragments.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    fun inject(fragment: MainFragment)
    fun inject(fragment: GraphFragment)
    fun getViewModelFactory(): ViewModelProvider.Factory
}