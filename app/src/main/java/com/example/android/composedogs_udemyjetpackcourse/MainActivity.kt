package com.example.android.composedogs_udemyjetpackcourse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android.composedogs_udemyjetpackcourse.presentation.screens.NavGraphs
import com.example.android.composedogs_udemyjetpackcourse.presentation.ui.theme.ComposeDogsUdemyJetpackCourseTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Myapp {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

@Composable
fun Myapp(content: @Composable () -> Unit) {
    ComposeDogsUdemyJetpackCourseTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}
