package com.example.android.composedogs_udemyjetpackcourse.presentation.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.android.composedogs_udemyjetpackcourse.domain.model.DogBreed

@Composable
fun DogDetailItem(
    dog: DogBreed,
    passImageColour: (Drawable) -> Unit,
) {
    val request = ImageRequest.Builder(LocalContext.current)
        .data(dog.imageUrl)
        .crossfade(true)
        .build()
    AsyncImage(
        model = request,
        contentDescription = null,
        modifier = Modifier
            .defaultMinSize(minHeight = 200.dp),
        onSuccess = {
            val image = it.result.drawable
            passImageColour(image)
        }
    )
    Text(
        text = dog.dogBreed ?: "",
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    )
    dog.origin?.let {
        if (it.isNotBlank()) {
            Text(text = it)
        }
    }
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = dog.bredFor ?: "")
        Text(text = dog.temperament ?: "", textAlign = TextAlign.Center)
        Text(text = dog.lifespan ?: "")
        Text(text = "${dog.weight?.metricWeight ?: ""} kilograms")
        Text(text = "${dog.height?.metricHeight ?: ""} centimetres")
    }
}