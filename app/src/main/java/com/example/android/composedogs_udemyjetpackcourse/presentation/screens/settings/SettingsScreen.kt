package com.example.android.composedogs_udemyjetpackcourse.presentation.screens.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android.composedogs_udemyjetpackcourse.presentation.screens.destinations.DogListScreenDestination
import com.example.android.composedogs_udemyjetpackcourse.presentation.screens.list.DogListViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SettingsScreen(
    viewModel: DogListViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    var showCustomDialog by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Settings") },
            navigationIcon = {
                IconButton(onClick = { navigator.navigate(DogListScreenDestination()) }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        )
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CacheDurationText(showDialog = { showCustomDialog = true })
            if (showCustomDialog) {
                CustomDialog(
                    dialogText = "Cached duration in seconds",
                    value = viewModel.cachedDurationState,
                    setDismissDialog = { showCustomDialog = it },
                    setValue = { viewModel.cachedDurationState = it },
                    updateCachedTime = { time ->
                        viewModel.setCachedDuration(time)
                    },
                    isSettingsScreen = true
                )
            }
        }
    }
}

@Composable
fun CustomDialog(
    dialogText: String,
    value: String = "",
    setDismissDialog: (Boolean) -> Unit,
    setValue: (String) -> Unit = {},
    updateCachedTime: (Int) -> Unit = {},
    isSettingsScreen: Boolean = false,
    isPermissionRequest: Boolean = false,
    onOkClickPermissionRequest: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = { setDismissDialog(false) }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colors.background
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = dialogText, modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable { setDismissDialog(false) }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    if (isSettingsScreen) {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(2.dp, color = Color.Green),
                                    shape = RoundedCornerShape(50)
                                ),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = null,
                                    tint = Color.Green.copy(alpha = 0.5f),
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(20.dp)
                                )
                            },
                            placeholder = { Text(text = "Enter time in seconds") },
                            value = value,
                            onValueChange = { setValue(it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            label = { Text(text = "Seconds") },
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        if (isSettingsScreen) {
                            Text(
                                text = "CANCEL",
                                modifier = Modifier.clickable { setDismissDialog(false) }
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                        Text(
                            text = "OK",
                            modifier = Modifier.clickable {
                                if (isSettingsScreen) {
                                    updateCachedTime(value.toInt())
                                }
                                setDismissDialog(false)
                                if (isPermissionRequest)
                                    onOkClickPermissionRequest()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CacheDurationText(
    showDialog: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color.Blue.copy(alpha = 0.5f))
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(50.dp))
            .clickable { showDialog() },
        border = BorderStroke(2.dp, Color.Blue),
        shape = RoundedCornerShape(50.dp),
        elevation = 50.dp,
    ) {
        Text(
            modifier = Modifier
                .background(Color.Blue.copy(alpha = 0.5f))
                .padding(8.dp),
            text = "Cached duration in seconds",
        )
    }
}