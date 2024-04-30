package com.example.daggerhilttest.workers

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.daggerhilttest.RandomWidget

class RandomWidgetWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val intent = Intent(applicationContext, RandomWidget::class.java).apply {
            action = "ACTION_UPDATE_WIDGET"
        }
        applicationContext.sendBroadcast(intent)
        return Result.success()

    }
}