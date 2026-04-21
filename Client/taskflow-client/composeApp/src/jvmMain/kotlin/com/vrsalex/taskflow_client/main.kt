package com.vrsalex.taskflow_client

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "taskflow_client",
    ) {
        App()
    }
}