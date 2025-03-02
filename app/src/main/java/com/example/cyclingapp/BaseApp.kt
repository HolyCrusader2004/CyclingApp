package com.example.cyclingapp

import android.app.Application
import com.example.cyclingapp.DependencyInjection.AppModule

class BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AppModule.provideDb(this)
    }
}