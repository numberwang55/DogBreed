package com.example.android.composedogs_udemyjetpackcourse.domain.model

import com.example.android.composedogs_udemyjetpackcourse.data.local.entity.DogBreedEntity

data class DogBreed(
    val dogBreed: String? = null,
    val id: Int? = null,
    val lifespan: String? = null,
    val origin: String? = null,
    val temperament: String? = null,
    val imageUrl: String? = null,
    val breedGroup: String? = null,
    val bredFor: String? = null,
    val weight: Weight? = null,
    val height: Height? =null
) {
    fun toDogBreedEntity(): DogBreedEntity {
        return DogBreedEntity(
            dogBreed,
            id!!,
            lifespan,
            origin,
            temperament,
            imageUrl,
            breedGroup,
            bredFor,
            weight,
            height
        )
    }
}
