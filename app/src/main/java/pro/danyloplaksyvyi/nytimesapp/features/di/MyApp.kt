package pro.danyloplaksyvyi.nytimesapp.features.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pro.danyloplaksyvyi.nytimesapp.R
import pro.danyloplaksyvyi.nytimesapp.features.main.data.api.BooksApiService
import pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.booksbylist.BooksByListRepository
import pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.booksbylist.BooksByListRepositoryImpl
import pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.overview.OverviewRepository
import pro.danyloplaksyvyi.nytimesapp.features.main.data.repository.overview.OverviewRepositoryImpl
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.booksbylist.BooksByListViewModel
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.overview.OverviewViewModel
import pro.danyloplaksyvyi.nytimesapp.features.room.data.AppDatabase
import pro.danyloplaksyvyi.nytimesapp.features.room.data.MIGRATION_1_2
import pro.danyloplaksyvyi.nytimesapp.features.room.data.MIGRATION_2_3
import pro.danyloplaksyvyi.nytimesapp.features.signin.data.GoogleAuthClient
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    // API Key (loaded once and shared)
    single<String>(qualifier = named("ApiKey")) {
        androidContext().loadApiKeyFromRaw() ?: throw IllegalStateException("API Key not found")
    }

    // Retrofit & API
    single { provideRetrofit() }
    single<BooksApiService> { get<Retrofit>().create(BooksApiService::class.java) }

    // Repository
    single<OverviewRepository> {
        OverviewRepositoryImpl(
            api = get(),
            apiKey = get(named("ApiKey")),
            listDao = get(),
            bookDao = get(),
            metadataDao = get()
        )
    }
    single<BooksByListRepository> {
        BooksByListRepositoryImpl(
            api = get(),
            bookDao = get(),
            listDao = get(),
            listMetadataDao = get(),
            apiKey = get(named("ApiKey"))
        )
    }

    // Other dependencies
    single { GoogleAuthClient(androidContext()) }

    // Room Database
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }
    single { get<AppDatabase>().listDao() }
    single { get<AppDatabase>().bookDao() }
    single { get<AppDatabase>().overviewMetadataDao() }
    single { get<AppDatabase>().listMetadataDao() }


    // ViewModels
    viewModelOf(::AuthViewModel)
    viewModelOf(::OverviewViewModel)
    viewModelOf(::BooksByListViewModel)
}

fun provideRetrofit(): Retrofit {
    // Create logging interceptor
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Add to OkHttpClient
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api.nytimes.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}

fun Context.loadApiKeyFromRaw(): String? {
    return try {
        val inputStream = resources.openRawResource(R.raw.secrets)
        val json = inputStream.bufferedReader().use { it.readText() }
        JSONObject(json).getString("nyt_api_key")
    } catch (e: Exception) {
        null
    }
}