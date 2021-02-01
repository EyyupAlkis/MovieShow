package dev.alks.movieshow.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.alks.movieshow.BuildConfig
import dev.alks.movieshow.data.datasource.MovieService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    val BASE_URL = "http://api.tvmaze.com/"

    @Provides
    @Singleton
    internal fun providesRetrofit(): Retrofit =
        createNetworkClient(BASE_URL, BuildConfig.DEBUG)
            .build()

    @Provides
    @Singleton
    internal fun provideGifService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)
}