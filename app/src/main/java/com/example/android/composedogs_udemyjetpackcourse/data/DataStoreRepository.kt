package com.example.android.composedogs_udemyjetpackcourse.data

import android.content.Context
import androidx.datastore.dataStore
import com.example.android.composedogs_udemyjetpackcourse.data.app_settings.AppSettings
import com.example.android.composedogs_udemyjetpackcourse.data.app_settings.AppSettingsSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore by dataStore("app-settings.json", AppSettingsSerializer)

class DataStoreRepository(context: Context) {

    private val dataStore = context.dataStore

    suspend fun setUpdateTime(time: Long) {
        dataStore.updateData {
            it.copy(
                StoredTime = time
            )
        }
    }

    suspend fun getUpdateTime(): Flow<Long> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(AppSettings())
                } else {
                    throw exception
                }
            }
            .map { appSettings ->
                val updateTime = appSettings.StoredTime
                updateTime
            }
    }

    suspend fun setCacheDuration(seconds: Int) {
        dataStore.updateData {
            it.copy(
                cacheDuration = seconds
            )
        }
    }

    suspend fun getCacheDuration(): Flow<Int> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(AppSettings())
                } else {
                    throw exception
                }
            }
            .map { appSettings ->
                val updateTime = appSettings.cacheDuration
                updateTime
            }
    }
}