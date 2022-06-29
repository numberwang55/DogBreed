package com.example.android.composedogs_udemyjetpackcourse.presentation.screens.list

import com.example.android.composedogs_udemyjetpackcourse.domain.model.DogBreed

data class DogListState(
    val dogs: List<DogBreed> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isRemote: String = ""
)
