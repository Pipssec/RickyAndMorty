package com.example.rickyandmorty.app

import android.app.Application
import com.example.rickyandmorty.di.DaggerAppComponent

class App: Application() {

    val appComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        appComponent.inject(this)
        super.onCreate()
    }

}