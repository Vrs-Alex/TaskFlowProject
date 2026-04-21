package com.vrsalex.taskflow_client

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform