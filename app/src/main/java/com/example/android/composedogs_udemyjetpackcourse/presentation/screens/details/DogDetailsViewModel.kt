package com.example.android.composedogs_udemyjetpackcourse.presentation.screens.details

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.android.composedogs_udemyjetpackcourse.data.local.DogDatabase
import com.example.android.composedogs_udemyjetpackcourse.domain.repository.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val database: DogDatabase
): ViewModel() {

    var state by mutableStateOf(DogDetailsState())

    init {
        val id = savedStateHandle.get<Int>("id")
        id?.let {
            getDogDetails(it)
        }
    }

    private fun getDogDetails(id: Int) {
        viewModelScope.launch {
            val result = database.dao.getDogById(id)
            state = state.copy(dog = result.toDogBreed())
        }
    }

    fun calculateDominantColour(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let {
                onFinish(Color(it))
            }
        }
    }
}