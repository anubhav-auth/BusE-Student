package com.example.buse_student

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class FcmTokenWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {
    val TAG = "MapsReading"
    override fun doWork(): Result {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                val timestamp = Timestamp.now()

                FirebaseFirestore.getInstance().collection("fcmTokens").document("$userID.androidId")
                    .set(hashMapOf("token" to token, "timestamp" to timestamp))
                    .addOnSuccessListener {
                        Log.d(TAG, "Token successfully stored.")
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error storing token.", exception)
                    }
            } else {

                Log.e(TAG, "Failed to get FCM token.")
            }
        }

        return Result.success()
    }
}
