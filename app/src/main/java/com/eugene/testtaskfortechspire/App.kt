package com.eugene.testtaskfortechspire

import android.app.Application
import com.eugene.testtaskfortechspire.di.AppComponent
import com.eugene.testtaskfortechspire.di.DaggerAppComponent
import com.eugene.testtaskfortechspire.di.modules.AppModule

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}