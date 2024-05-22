package com.example.buse_student

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.buse_student.ui.theme.BusEStudentTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val TAG = "MapsReading"


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        userID.androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        FirebaseApp.initializeApp(this)
        val database = Firebase.database
        val myRef = database.getReference("locations")
        Firebase.messaging.isAutoInitEnabled = true


        val workRequest = PeriodicWorkRequestBuilder<FcmTokenWorker>(5, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "FetchAndStoreFcmToken",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        NotificationUtils.createNotificationChannel(this)

        var allPermissionsGranted by mutableStateOf(false)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                allPermissionsGranted = permissions.values.all { it }

            }
        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.POST_NOTIFICATIONS,
                android.Manifest.permission.FOREGROUND_SERVICE
            )


        )
        Log.d(TAG, "oncreate main")

        setContent {


            if (allPermissionsGranted) {
                NotificationUtils.showNotification(this@MainActivity, "hello", "hi")
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    }) {
                        Text(text = "Launch map")
                    }
                }
            } else {

                Toast.makeText(
                    this@MainActivity,
                    "Please grant all permissions.",
                    Toast.LENGTH_LONG
                ).show()


            }
        }
    }



}