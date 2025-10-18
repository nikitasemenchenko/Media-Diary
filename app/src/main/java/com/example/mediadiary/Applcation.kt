package com.example.mediadiary

import android.app.Application
import com.example.mediadiary.data.di.AppContainer

class MediaDiaryApplication: Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}