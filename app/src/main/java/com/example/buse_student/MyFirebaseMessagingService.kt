package com.example.buse_student

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.concurrent.TimeUnit

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MapsReading"
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val title = it.title?: ""
            val body = it.body?: ""
            NotificationUtils.showNotification(this, title, body)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "Refreshed token: $token")

        FirebaseMessaging.getInstance().subscribeToTopic("driver-left")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Subscribed to topic successfully.")
                } else {
                    Log.w(TAG, "Subscription to topic failed.", task.exception)
                }
            }

        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        val tokenMap = hashMapOf(
            "token" to token,
            "timestamp" to Timestamp.now()
        )


        FirebaseFirestore.getInstance().collection("fcmTokens").document("$userID.androidId")
            .set(tokenMap)
            .addOnSuccessListener {
                Log.d(TAG, "Token successfully stored.")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error storing token.", exception)
            }
    }


}
