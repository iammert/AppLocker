package com.momentolabs.app.security.applocker.service.worker

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkerStarter {

    private const val UNIQUE_WORK_SERVICE_CHECKER = "UNIQUE_WORK_SERVICE_CHECKER"

    fun startServiceCheckerWorker() {
        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                UNIQUE_WORK_SERVICE_CHECKER,
                ExistingPeriodicWorkPolicy.REPLACE,
                PeriodicWorkRequestBuilder<ServiceCheckerWorker>(16, TimeUnit.MINUTES).build()
            )
    }


}