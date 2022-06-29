package com.example.android.composedogs_udemyjetpackcourse.data.remote

import com.example.android.composedogs_udemyjetpackcourse.data.remote.dto.DogBreedDto
import retrofit2.http.GET

interface DogApi {

    @GET("DevTides/DogsApi/master/dogs.json")
    suspend fun getDogsList(): List<DogBreedDto>

}