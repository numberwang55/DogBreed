package com.example.android.composedogs_udemyjetpackcourse.domain.repository

import com.example.android.composedogs_udemyjetpackcourse.domain.model.DogBreed
import com.example.android.composedogs_udemyjetpackcourse.util.Resource
import kotlinx.coroutines.flow.Flow

interface DogRepository {

    suspend fun getAllDogsFromBackend(): Flow<Resource<List<DogBreed>>>

    suspend fun getAllDogsFromDatabase(): Flow<Resource<List<DogBreed>>>

    suspend fun getDogById(id: Int): Resource<DogBreed>
}