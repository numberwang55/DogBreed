package com.example.android.composedogs_udemyjetpackcourse.presentation.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import coil.Coil
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.android.composedogs_udemyjetpackcourse.domain.model.DogBreed

@Composable
fun DogListItem(
    dog: DogBreed,
    modifier: Modifier = Modifier,
) {
    val defaultDominantColour = MaterialTheme.colors.surface
    var dominantColour by remember { mutableStateOf(defaultDominantColour) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            dominantColour,
                            defaultDominantColour
                        )
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
            ) {
            val request = ImageRequest.Builder(LocalContext.current)
                .data(dog.imageUrl)
                .crossfade(true)
                .build()
            AsyncImage(
                model = request,
                contentDescription = null,
                modifier = Modifier
                    .height(125.dp)
                    .padding(4.dp)
                    .weight(2f),
                onSuccess = {
                    val image = it.result.drawable
                    calculateDominantColour(image){ colour ->
                        dominantColour = colour
                    }
                }
            )
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 4.dp)
            ) {
                Text(text = dog.dogBreed ?: "", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Text(text = dog.lifespan ?: "")
            }
        }
    }
}

private fun calculateDominantColour(drawable: Drawable, onFinish: (Color) -> Unit) {
    val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
    Palette.from(bmp).generate { palette ->
        palette?.dominantSwatch?.rgb?.let {
            onFinish(Color(it))
        }
    }
}