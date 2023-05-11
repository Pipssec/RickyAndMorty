package com.example.rickyandmorty.app

import android.app.Application
import com.example.rickyandmorty.di.DaggerAppComponent

class App: Application() {

    val appComponent = DaggerAppComponent.factory().create(this)

}