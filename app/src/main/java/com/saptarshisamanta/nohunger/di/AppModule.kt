package com.saptarshisamanta.nohunger.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.saptarshisamanta.nohunger.api.FoodApiService
import com.saptarshisamanta.nohunger.util.Constants.Companion.AC_TYPE
import com.saptarshisamanta.nohunger.util.Constants.Companion.BASE_URL
import com.saptarshisamanta.nohunger.util.Constants.Companion.KEY_AC_FIRST_TIME
import com.saptarshisamanta.nohunger.util.Constants.Companion.TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @NewSharedPreferences
    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext
        app: Context
    ): SharedPreferences = app.getSharedPreferences(AC_TYPE, MODE_PRIVATE)

    @TokenSharedPreferences
    @Singleton
    @Provides
    fun provideTokenSharedPreferences(
        @ApplicationContext
        app: Context
    ): SharedPreferences = app.getSharedPreferences(TOKEN, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideIsFirstTime(@NewSharedPreferences sharedPreferences: SharedPreferences) =
        sharedPreferences.getBoolean(
            KEY_AC_FIRST_TIME, true
        )

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideFoodApi(retrofit: Retrofit): FoodApiService =
        retrofit.create(FoodApiService::class.java)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewSharedPreferences

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenSharedPreferences


