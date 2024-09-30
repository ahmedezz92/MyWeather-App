package com.example.myweatherapp.di

import com.example.myweatherapp.domain.repository.WeatherRepository
import com.example.myweatherapp.core.data.module.NetworkModule
import com.example.myweatherapp.data.remote.api.WeatherApiService
import com.example.myweatherapp.data.repository.WeatherRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideHomeApi(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(homeService: WeatherApiService): WeatherRepository {
        return WeatherRepositoryImp(homeService)
    }
}