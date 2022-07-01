package com.example.android.composedogs_udemyjetpackcourse.presentation.screens.details

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.android.composedogs_udemyjetpackcourse.MainActivity
import com.example.android.composedogs_udemyjetpackcourse.R
import com.example.android.composedogs_udemyjetpackcourse.domain.model.SmsInfo
import com.example.android.composedogs_udemyjetpackcourse.presentation.components.DogDetailItem
import com.example.android.composedogs_udemyjetpackcourse.presentation.components.PermissionsComposable
import com.example.android.composedogs_udemyjetpackcourse.util.isPermanentlyDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalPermissionsApi::class)
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
    var showPermissionDialog by remember { mutableStateOf(false) }
    val permissionState =
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.SEND_SMS
            )
        )
    var showSmsDialog by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var smsText by remember { mutableStateOf("") }
    val context = LocalContext.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dog Details") },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                if (permissionState.permissions.first().status.isGranted) {
                                    showSmsDialog = true
                                } else if (permissionState.permissions
                                        .first()
                                        .isPermanentlyDenied() ||
                                    permissionState.shouldShowRationale
                                ) {
                                    permissionState.launchMultiplePermissionRequest()
                                    showPermissionDialog = true
                                }
                            }
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_SEND)
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog breed")
                                intent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    "${dogDetailState.dogBreed} was bred for ${dogDetailState.bredFor}"
                                )
                                intent.putExtra(Intent.EXTRA_STREAM, dogDetailState.imageUrl)
                                context.startActivity(intent)
                            }
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
            if (showPermissionDialog) {
                PermissionsComposable(
                    permissionState = permissionState,
                    setDismissDialog = { showPermissionDialog = it },
                    onOkClickPermissionRequestShowRationale = { permissionState.launchMultiplePermissionRequest() }
                )
            }
            if (showSmsDialog) {
                SmsDialog(
                    dialogText = "Share via SMS",
                    setDismissDialog = { showSmsDialog = it },
                    textFieldValue = smsText,
                    onTextFieldValueChange = { smsText = it },
                    onSendSms = {
                        val smsInfo = SmsInfo(
                            smsText,
                            "${dogDetailState.dogBreed} was bred for ${dogDetailState.bredFor}",
                            dogDetailState.imageUrl
                        )
                        sendSms(smsInfo = smsInfo, context)
                    }
                )
            }
        }
    }
}

@Composable
fun SmsDialog(
    dialogText: String,
    setDismissDialog: (Boolean) -> Unit,
    textFieldValue: String,
    onTextFieldValueChange: (String) -> Unit,
    onSendSms: () -> Unit
) {
    Dialog(
        onDismissRequest = { setDismissDialog(false) }
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
            elevation = 8.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = dialogText,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            modifier = Modifier.clickable { setDismissDialog(false) }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                BorderStroke(1.dp, color = Color.Blue),
                                shape = RoundedCornerShape(50)
                            ),
                        value = textFieldValue,
                        onValueChange = {
                            onTextFieldValueChange(it)
                        },
                        label = { Text(text = "To:") },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = { Text(text = "Enter phone number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                    Image(
                        painter = painterResource(id = R.drawable.dog),
                        contentDescription = null
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "CANCEL",
                            modifier = Modifier.clickable {
                                setDismissDialog(false)
                            }
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = "SEND SMS",
                            modifier = Modifier.clickable {
                                onSendSms()
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun sendSms(
    smsInfo: SmsInfo,
    context: Context
) {
    val intent = Intent(context, MainActivity::class.java)
    val pi = PendingIntent.getActivity(context,0, intent, 0)
    val sms = SmsManager.getDefault()
    sms.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null)
}