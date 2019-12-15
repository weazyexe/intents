package exe.weazy.intents.util

import android.app.Application
import exe.weazy.intents.di.AppComponent
import exe.weazy.intents.di.DaggerAppComponent
import exe.weazy.intents.di.DatabaseModule

class App : Application() {

    companion object {
        private lateinit var component : AppComponent

        fun getComponent() = component
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(applicationContext))
            .build()
    }
}