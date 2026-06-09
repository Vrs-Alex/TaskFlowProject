package vrsalex.notify.data

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification

class FcmProvider {

    init {
        initFirebase()
    }

    fun sendPush(
        tokens: List<String>,
        title: String, body: String
    ) {
        if (tokens.isEmpty()) return

        val notification = Notification.builder()
            .setTitle(title)
            .setBody(body)
            .build()

        val message = MulticastMessage.builder()
            .setNotification(notification)
            .addAllTokens(tokens)
            .build()

        val response = FirebaseMessaging.getInstance().sendEachForMulticast(message)

        response.responses.forEachIndexed { index, result ->
            if (!result.isSuccessful) {
                val failedToken = tokens[index]

            }
        }
    }

    private fun initFirebase() {
        val serviceAccount = object {}.javaClass.classLoader
            .getResourceAsStream("fcm.json")
            ?: error("fms.json not found")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        FirebaseApp.initializeApp(options)
    }

}