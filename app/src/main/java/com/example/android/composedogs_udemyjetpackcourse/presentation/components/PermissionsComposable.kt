package com.example.android.composedogs_udemyjetpackcourse.presentation.components

import android.Manifest
import android.util.Log
import androidx.compose.runtime.Composable
import com.example.android.composedogs_udemyjetpackcourse.presentation.screens.settings.CustomDialog
import com.example.android.composedogs_udemyjetpackcourse.util.isPermanentlyDenied
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsComposable(
    permissionState: MultiplePermissionsState,
    onOkClickPermissionRequestShowRationale: () -> Unit = {},
    setDismissDialog: (Boolean) -> Unit
) {
    permissionState.permissions.forEach { permission ->
        when (permission.permission) {
            Manifest.permission.SEND_SMS -> {
                when {
                    permission.status.isGranted -> {
                        CustomDialog(
                            dialogText = "SMS permission granted. Thank you.",
                            setDismissDialog = { setDismissDialog(false) },
                            isPermissionRequest = true,
                            onOkClickPermissionRequest = onOkClickPermissionRequestShowRationale
                        )
                        Log.d("TAG", "PermissionsComposable: Sms permission granted")
                    }
                    permission.status.shouldShowRationale -> {
                        CustomDialog(
                            dialogText = "SMS permission is required to send dog details",
                            setDismissDialog = { setDismissDialog(false) },
                            isPermissionRequest = true,
                            onOkClickPermissionRequest = onOkClickPermissionRequestShowRationale
                        )
                        Log.d("TAG", "PermissionsComposable: Sms permission is required to send dog details")
                    }
                    permission.isPermanentlyDenied() -> {
                        CustomDialog(
                            dialogText = "SMS permission was denied. To change this go to android settings",
                            setDismissDialog = { setDismissDialog(false) },
                            isPermissionRequest = true,
                            onOkClickPermissionRequest = onOkClickPermissionRequestShowRationale
                        )
                        Log.d("TAG", "Sms permission was denied. To change this go to android settings")
                    }
                }
            }
        }
    }
}