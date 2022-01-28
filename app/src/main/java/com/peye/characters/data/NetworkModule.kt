package com.peye.characters.data

import android.content.Context
import com.peye.characters.BuildConfig
import com.peye.characters.data.api.CharacterApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val NETWORK_TIMEOUT_READ_SEC = 15L
private const val NETWORK_TIMEOUT_CONNECT_SEC = 15L

fun Module.networkModule() {
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideCharacterApiService(get()) }
}

private fun provideOkHttpClient(context: Context): OkHttpClient {
    val builder = OkHttpClient.Builder().apply {
        readTimeout(NETWORK_TIMEOUT_READ_SEC, TimeUnit.SECONDS)
        connectTimeout(NETWORK_TIMEOUT_CONNECT_SEC, TimeUnit.SECONDS)

        if (BuildConfig.ENABLE_HTTP_LOGGING) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(loggingInterceptor)
        }
    }
    return builder.build()
}

private fun provideRetrofit(client: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.CHARACTER_API_BASE_URL)
        .build()

private fun provideCharacterApiService(retrofit: Retrofit): CharacterApiService =
    retrofit.create(CharacterApiService::class.java)
