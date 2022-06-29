package com.example.android.composedogs_udemyjetpackcourse.presentation.screens.details

import com.example.android.composedogs_udemyjetpackcourse.domain.model.DogBreed

data class DogDetailsState(
    val dog: DogBreed = DogBreed(),
    val isLoading: Boolean = false
)
