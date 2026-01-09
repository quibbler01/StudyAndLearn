package cn.quibbler.coroutine

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.bind
import org.koin.dsl.module
import cn.quibbler.coroutine.BuildConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)

            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)

            module {
                single { this@App } bind Context::class
            }
        }
//        registerActivityLifecycleCallbacks()
    }

}