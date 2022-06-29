package com.example.android.composedogs_udemyjetpackcourse.data.remote.dto

import com.example.android.composedogs_udemyjetpackcourse.domain.model.DogBreed

data class DogBreedDto(
    val bred_for: String,
    val breed_group: String,
    val country_code: String,
    val description: String,
    val height: HeightDto,
    val history: String,
    val id: Int,
    val life_span: String,
    val name: String,
    val origin: String,
    val temperament: String,
    val url: String,
    val weight: WeightDto
) {
    fun toDogBreed(): DogBreed {
        return DogBreed(
            name,
            id,
            life_span,
            origin,
            temperament,
            url,
            breed_group,
            bred_for,
            weight.toWeight(),
            height.toHeight()
        )
    }
}