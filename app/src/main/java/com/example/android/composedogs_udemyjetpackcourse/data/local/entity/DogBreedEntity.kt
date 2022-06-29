package com.example.android.composedogs_udemyjetpackcourse.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.composedogs_udemyjetpackcourse.data.remote.dto.HeightDto
import com.example.android.composedogs_udemyjetpackcourse.data.remote.dto.WeightDto
import com.example.android.composedogs_udemyjetpackcourse.domain.model.DogBreed
import com.example.android.composedogs_udemyjetpackcourse.domain.model.Height
import com.example.android.composedogs_udemyjetpackcourse.domain.model.Weight

@Entity(tableName = "DogBreed")
data class DogBreedEntity(
    val dogBreed: String? = null,
    @PrimaryKey
    val id: Int,
    val lifespan: String? = null,
    val origin: String? = null,
    val temperament: String? = null,
    val imageUrl: String? = null,
    val breedGroup: String? = null,
    val bredFor: String? = null,
    @Embedded
    val weight: Weight? = null,
    @Embedded
    val height: Height? = null
) {
    fun toDogBreed(): DogBreed {
        return DogBreed(
            dogBreed,
            id,
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
