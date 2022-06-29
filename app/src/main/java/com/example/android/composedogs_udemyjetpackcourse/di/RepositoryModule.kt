package com.example.android.composedogs_udemyjetpackcourse.di

import com.example.android.composedogs_udemyjetpackcourse.data.repository.DogRepositoryImpl
import com.example.android.composedogs_udemyjetpackcourse.domain.repository.DogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDogRepository(
        dogRepositoryImpl: DogRepositoryImpl
    ): DogRepository

}