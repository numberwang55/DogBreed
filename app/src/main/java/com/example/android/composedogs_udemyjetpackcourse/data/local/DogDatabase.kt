package com.example.android.composedogs_udemyjetpackcourse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android.composedogs_udemyjetpackcourse.data.local.entity.DogBreedEntity

@Database(entities = [DogBreedEntity::class], version = 1)
abstract class DogDatabase: RoomDatabase() {

    abstract val dao: DogDao
}