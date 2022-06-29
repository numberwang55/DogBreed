package com.example.android.composedogs_udemyjetpackcourse.presentation.screens.list

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.composedogs_udemyjetpackcourse.data.DataStoreRepository
import com.example.android.composedogs_udemyjetpackcourse.domain.repository.DogRepository
import com.example.android.composedogs_udemyjetpackcourse.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class DogListViewModel @Inject constructor(
    private val repository: DogRepository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
): AndroidViewModel(application) {

    var state by mutableStateOf(DogListState())
    var refreshTime = 1 * 60 * 1000 * 1000 * 1000L
    var timeState by mutableStateOf(0L)
    var cachedDurationState by mutableStateOf("0")

    init {
        refresh()
    }

    private fun refresh() {
        checkCacheDuration()
        viewModelScope.launch {
            dataStoreRepository.getUpdateTime().collect {
                val updateTime = it
                if (updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
                    fetchFromDatabase()
                } else {
                    fetchFromRemote()
                }
            }
        }
    }

    private fun checkCacheDuration() {
        viewModelScope.launch {
            dataStoreRepository.getCacheDuration().collect { cacheDuration ->
                try {
                    cachedDurationState = cacheDuration.toString()
                    refreshTime = cacheDuration.times(1000 * 1000 * 1000L)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
                dataStoreRepository.getUpdateTime().collect {
                    timeState = it
                }
            }
        }
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        viewModelScope.launch {
            dataStoreRepository.setUpdateTime(System.nanoTime())
            repository.getAllDogsFromBackend().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            state = state.copy(
                                dogs = it,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        state = state.copy(isLoading = result.isLoading)
                    }
                    is Resource.Error -> {
                        Log.d("TAG", "getDogsList: ${result.message}")
                    }
                }
            }
        }
        Toast.makeText(getApplication(), "Fetched from Remote Source", Toast.LENGTH_SHORT).show()
    }

    private fun fetchFromDatabase() {
        viewModelScope.launch {
            repository.getAllDogsFromDatabase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let {
                            state = state.copy(
                                dogs = it,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        state = state.copy(isLoading = result.isLoading)
                    }
                    is Resource.Error -> {
                        Log.d("TAG", "getDogsList: ${result.message}")
                    }
                }
            }
        }
//        Toast.makeText(getApplication(), "Fetched from Database", Toast.LENGTH_SHORT).show()
    }

    fun setCachedDuration(seconds: Int) {
        viewModelScope.launch {
            dataStoreRepository.setCacheDuration(seconds)
        }
    }

}