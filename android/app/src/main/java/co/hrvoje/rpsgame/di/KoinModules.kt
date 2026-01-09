package co.hrvoje.rpsgame.di

import co.hrvoje.rpsgame.data.network.services.AuthService
import co.hrvoje.rpsgame.data.network.ws.WSAuthService
import co.hrvoje.rpsgame.data.network.ws.api.AuthAPI
import co.hrvoje.rpsgame.navigation.AppNavigator
import co.hrvoje.rpsgame.utils.Constants
import co.hrvoje.rpsgame.utils.CurrentUser
import co.hrvoje.rpsgame.viewmodel.login.LoginViewModel
import co.hrvoje.rpsgame.viewmodel.register.RegisterViewModel
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val modules = module {
    singleOf(::AppNavigator).bind<AppNavigator>()
    singleOf(::WSAuthService).bind<AuthService>()
    singleOf(::CurrentUser).bind<CurrentUser>()

    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)

    single {
        createRetrofit(
            okHttpClient = createDefaultOkHttpClient().build()
        )
    }

    single { get<Retrofit>().create(AuthAPI::class.java) }
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
