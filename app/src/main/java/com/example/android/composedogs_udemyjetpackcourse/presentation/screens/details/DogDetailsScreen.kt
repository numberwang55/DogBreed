package com.example.android.composedogs_udemyjetpackcourse.presentation.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.android.composedogs_udemyjetpackcourse.presentation.components.DogDetailItem
import com.example.android.composedogs_udemyjetpackcourse.presentation.screens.destinations.DogListScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun DogDetailsScreen(
    id: Int,
    navigator: DestinationsNavigator,
    viewModel: DogDetailsViewModel = hiltViewModel()
) {
    val dogDetailState = viewModel.state.dog
    val defaultDominantColour = MaterialTheme.colors.surface
    var dominantColour by remember { mutableStateOf(defaultDominantColour) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dog Details") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigate(DogListScreenDestination()) }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            dominantColour,
                            defaultDominantColour
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DogDetailItem(dog = dogDetailState) { drawable ->
                viewModel.calculateDominantColour(drawable) { colour ->
                    dominantColour = colour
                }
            }
        }
    }
}