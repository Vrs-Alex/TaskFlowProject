package com.vrsalex.taskflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.vrsalex.taskflow.presentation.navigation.TaskFlowEntyPoint
import com.vrsalex.uikit.preview.UikitPreview


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().setKeepOnScreenCondition { false }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false


        setContent {
            TaskFlowEntyPoint()
        }
    }
}
