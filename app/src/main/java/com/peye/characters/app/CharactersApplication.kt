package com.peye.characters.app

import android.app.Application
import android.util.Log
import androidx.databinding.ktx.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import timber.log.Timber

class CharactersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        startKoin()
    }

    private fun initTimber() {
        val tree = if (BuildConfig.DEBUG) Timber.DebugTree() else ProductionTree()
        Timber.plant(tree)
    }

    private fun startKoin() = org.koin.core.context.startKoin {
        // See https://giters.com/InsertKoinIO/koin/issues/1188
        androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
        androidContext(this@CharactersApplication)
        modules(getAppModule())
    }
}

private class ProductionTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority != Log.ERROR) {
            return
        }
        super.log(priority, tag, message, t)
    }
}
