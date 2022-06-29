package com.example.android.composedogs_udemyjetpackcourse.presentation.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.composedogs_udemyjetpackcourse.domain.model.DogBreed
import com.example.android.composedogs_udemyjetpackcourse.presentation.components.DogListItem
import com.example.android.composedogs_udemyjetpackcourse.presentation.screens.destinations.DogDetailsScreenDestination
import com.example.android.composedogs_udemyjetpackcourse.presentation.screens.destinations.SettingsScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Destination(start = true)
@Composable
fun DogListScreen(
    navigator: DestinationsNavigator,
    viewModel: DogListViewModel = hiltViewModel()
) {
    val expanded = remember { mutableStateOf(false) }
    val state = viewModel.state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dogs List") },
                actions = {
                    IconButton(onClick = { expanded.value = true }) {
                        Icon(imageVector = Icons.Default.List, contentDescription = null)
                    }
                }
            )
        },
    ) { paddingValues ->
        ShowSettingsDropDownMeu(
            expanded = expanded.value,
            onExpandedDismiss = {
                expanded.value = false
            },
            onSettingsClick = { navigator.navigate(SettingsScreenDestination()) }
        )
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.refreshBypassCache() },
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    contentPadding = paddingValues,
                    state = scrollState,
                ) {
                    items(state.dogs) { dog ->
                        DogListItem(
                            dog = dog,
                            modifier = Modifier
                                .clickable {
                                    dog.id?.let { navigator.navigate(DogDetailsScreenDestination(it)) }
                                },
                        )
                    }
                }
            }
            NavigateByCharacterColumn(
                coroutineScope = coroutineScope,
                scrollState = scrollState,
                state = state.dogs,
            )
        }
    }
}

@Composable
fun ShowSettingsDropDownMeu(
    expanded: Boolean,
    onExpandedDismiss: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .absolutePadding(right = 4.dp)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onExpandedDismiss()
            },
            modifier = Modifier
                .width(140.dp)
                .background(Color.White)
        ) {
            DropdownMenuItem(
                onClick = {
                    onExpandedDismiss()
                    onSettingsClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color.LightGray
                )
                Text(
                    text = " Settings",
                    fontWeight = FontWeight(300)
                )
            }
        }
    }
}

@Composable
fun NavigateByCharacterColumn(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    scrollState: LazyListState,
    state: List<DogBreed>,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .background(Color.Black)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(start = 8.dp)
    ) {
//        val alphabetCharactersList = listOf(
//            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
//            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
//        )
        val alphabetCharactersList = mutableListOf<Char>()
        state.forEach { dogBreed ->
            val character = dogBreed.dogBreed?.get(0)
            if (!alphabetCharactersList.contains(character ?: 'A')) {
                alphabetCharactersList.add(character!!)
            }
        }
        alphabetCharactersList.forEach {
            CustomButton(
                coroutineScope = coroutineScope,
                scrollState = scrollState,
                state = state,
                character = it
            )
        }
    }
}

@Composable
fun CustomButton(
    coroutineScope: CoroutineScope,
    scrollState: LazyListState,
    state: List<DogBreed>,
    character: Char
) {
    Button(
        onClick = {
            coroutineScope.launch {
                val characterToFilter = state.indexOfFirst {
                    it.dogBreed?.startsWith(character)!!
                }
                scrollState.scrollToItem(characterToFilter)
            }
        },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
        modifier = Modifier.size(width = 45.dp, height = 45.dp)
    ) {
        Text(text = character.toString())
    }
}