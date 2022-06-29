package com.example.android.composedogs_udemyjetpackcourse.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.composedogs_udemyjetpackcourse.data.local.entity.DogBreedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDao {

    @Query("SELECT * FROM dogbreed")
    suspend fun getDogs(): List<DogBreedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDogs(dogs: List<DogBreedEntity>)

    @Query("SELECT * FROM dogbreed WHERE id LIKE :id")
    suspend fun getDogById(id: Int): DogBreedEntity

    @Query("DELETE FROM dogbreed")
    suspend fun deleteDogs()
}