package co.hrvoje.rpsgame.di

import co.hrvoje.rpsgame.navigation.AppNavigator
import co.hrvoje.rpsgame.utils.Constants
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val modules = module {
    singleOf(::AppNavigator).bind<AppNavigator>()

    single {
        createRetrofit(
            okHttpClient = createDefaultOkHttpClient().build()
        )
    }
}

private fun createDefaultOkHttpClient(): OkHttpClient.Builder =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)


private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit
        .Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
