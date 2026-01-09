package co.hrvoje.rpsgame.app

import android.app.Application
import co.hrvoje.rpsgame.di.modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RockPaperScissorsGameApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RockPaperScissorsGameApp)
            modules(modules)
        }
    }
}
