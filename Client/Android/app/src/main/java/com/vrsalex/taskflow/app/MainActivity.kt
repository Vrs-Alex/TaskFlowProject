package com.vrsalex.taskflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vrsalex.uikit.preview.UikitPreview
import vrsalex.api.dto.event.EventItemCreateRequest


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            UikitPreview()
            EventItemCreateRequest
        }
    }
}
