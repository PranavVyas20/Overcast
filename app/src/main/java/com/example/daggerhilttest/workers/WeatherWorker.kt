package com.example.daggerhilttest.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorker
import com.example.daggerhilttest.R
import com.example.daggerhilttest.activities.MainActivity
import com.example.daggerhilttest.location.LocationManager
import com.example.daggerhilttest.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import javax.inject.Inject
import kotlin.random.Random

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationManager: LocationManager,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val notificationChannelId = "AndroidWeatherNotificationChannelId"

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            0, createNotification()
        )
    }

    private fun createNotification(): Notification {
        createNotificationChannel()

        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)

        val mainActivityPendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                mainActivityIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )

        return NotificationCompat.Builder(applicationContext, notificationChannelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText("Weather data fetched" + Random.nextInt())
            .setContentIntent(mainActivityPendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun createNotificationChannel() {
        val notificationChannel =
            NotificationChannel(
                notificationChannelId,
                "Sync Articles",
                NotificationManager.IMPORTANCE_DEFAULT,
            )

        val notificationManager: NotificationManager? =
            ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)

        notificationManager?.createNotificationChannel(notificationChannel)
    }

    @SuppressLint("InlinedApi")
    private fun hasNotificationPermission() = ActivityCompat.checkSelfPermission(
        applicationContext,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private suspend fun getWeatherData(location: Location) =
        weatherRepository.getWeather(
            lat = location.latitude.toFloat(),
            long = location.longitude.toFloat()
        )


    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        Log.d("worker_tag", "work started")
        locationManager.getLastKnownLocation().onSuccess {
            Log.d("worker_tag", "got location")
            getWeatherData(location = it).onSuccess {
                if (hasNotificationPermission()) {
                    with(NotificationManagerCompat.from(applicationContext)) {
                        notify(0, createNotification())
                    }
                    return Result.success()
                }
            }
        }.onFailure {
            Log.d("worker_tag", "error in location: ${it.message}")
        }
        return Result.failure()
    }
}