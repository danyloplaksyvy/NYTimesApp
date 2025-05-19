package pro.danyloplaksyvyi.nytimesapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import pro.danyloplaksyvyi.nytimesapp.features.signin.data.GoogleAuthClient
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}

val appModule = module {
    single { GoogleAuthClient(androidContext()) }
    viewModelOf(::AuthViewModel)
}