package com.momentolabs.app.security.applocker.service.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.momentolabs.app.security.applocker.service.ServiceStarter
import com.momentolabs.app.security.applocker.service.logger.ServiceLogger

class ServiceCheckerWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        ServiceStarter.startService(applicationContext)
        return Result.success()
    }
}