package com.example.android.composedogs_udemyjetpackcourse.data.repository

import com.example.android.composedogs_udemyjetpackcourse.data.local.DogDatabase
import com.example.android.composedogs_udemyjetpackcourse.data.remote.DogApi
import com.example.android.composedogs_udemyjetpackcourse.domain.model.DogBreed
import com.example.android.composedogs_udemyjetpackcourse.domain.repository.DogRepository
import com.example.android.composedogs_udemyjetpackcourse.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DogRepositoryImpl @Inject constructor(
    private val api: DogApi,
    private val database: DogDatabase
) : DogRepository {

    private val dao = database.dao

    override suspend fun getAllDogsFromBackend(): Flow<Resource<List<DogBreed>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            try {
                val remoteResultList = api.getDogsList().map { it.toDogBreed() }
                emit(
                    Resource.Success(
                        data = remoteResultList,
                        message = "remote"
                    )
                )
                dao.deleteDogs()
                dao.addDogs(remoteResultList.map { it.toDogBreedEntity() })
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Error"))
            }
            emit(Resource.Loading(isLoading = false))
        }
    }


    override suspend fun getAllDogsFromDatabase(): Flow<Resource<List<DogBreed>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val localResultList = dao.getDogs()
            if (localResultList.isEmpty()) {
                emit(Resource.Loading(false))
                return@flow
            }
            emit(
                Resource.Success(
                    data = localResultList.map { it.toDogBreed() },
                    message = "database"
                )
            )
            emit(Resource.Loading(false))
        }
    }

//override suspend fun getAllDogs(
//    fetchFromRemote: Boolean
//): Flow<Resource<List<DogBreed>>> {
//    return flow {
//        emit(Resource.Loading(isLoading = true))
//        val localListings = dao.getDogs()
//        emit(
//            Resource.Success(
//                data = localListings.map { it.toDogBreed() },
//                message = "database"
//            )
//        )
//        val isDbEmpty = localListings.isEmpty()
//        val shouldLoadDataFromCache = !isDbEmpty && !fetchFromRemote
//        if (shouldLoadDataFromCache) {
//            emit(Resource.Loading(false))
//            return@flow
//        }
//        val remoteResults = try {
//            api.getDogsList()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            emit(Resource.Error("Couldn't load data"))
//            null
//        } catch (e: HttpException) {
//            e.printStackTrace()
//            emit(Resource.Error("Couldn't load data"))
//            null
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emit(Resource.Error(e.message ?: "Error"))
//            null
//        }
//        remoteResults?.let { dogList ->
//            dao.deleteDogs()
//            dao.addDogs(dogList.map { it.toDogBreed().toDogBreedEntity() })
//            emit(
//                Resource.Success(
//                    data = dao.getDogs().map { it.toDogBreed() },
//                    message = "remote"
//                )
//            )
//            emit(Resource.Loading(isLoading = false))
//        }
//    }
//}

    override suspend fun getDogById(id: Int): Resource<DogBreed> {
        return try {
            val response = dao.getDogById(id)
            Resource.Success(response.toDogBreed())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "Error")
        }
    }

}