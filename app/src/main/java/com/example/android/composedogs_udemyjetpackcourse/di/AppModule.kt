package com.example.android.composedogs_udemyjetpackcourse.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.android.composedogs_udemyjetpackcourse.data.DataStoreRepository
import com.example.android.composedogs_udemyjetpackcourse.data.local.DogDatabase
import com.example.android.composedogs_udemyjetpackcourse.data.remote.DogApi
import com.example.android.composedogs_udemyjetpackcourse.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ): DataStoreRepository {
        return DataStoreRepository(context)
    }

    @Provides
    @Singleton
    fun provideDogApi(): DogApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDogDatabase(app: Application): DogDatabase {
        return Room.databaseBuilder(
            app,
            DogDatabase::class.java,
            "dog_database"
        ).build()
    }

}