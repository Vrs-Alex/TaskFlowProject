package com.vrsalex.taskflow.data.notify

import com.google.firebase.messaging.FirebaseMessaging
import com.vrsalex.taskflow.domain.notify.PushTokenProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FcmTokenProvider : PushTokenProvider {
    override suspend fun getToken(): String = suspendCancellableCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { continuation.resume(it) }
            .addOnFailureListener { continuation.resume("") }
    }
}