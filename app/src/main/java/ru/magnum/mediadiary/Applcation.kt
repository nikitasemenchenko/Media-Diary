package ru.magnum.mediadiary

import android.app.Application
import ru.magnum.mediadiary.data.di.AppContainer

class MediaDiaryApplication: Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}