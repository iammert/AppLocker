package com.momentolabs.app.security.applocker.service.stateprovider

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import com.momentolabs.app.security.applocker.ui.permissions.PermissionChecker
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.app.ActivityManager
import androidx.annotation.RequiresApi
import com.momentolabs.app.security.applocker.ui.overlay.activity.OverlayValidationActivity


class AppForegroundObservable @Inject constructor(val context: Context) {

    private var foregroundFlowable: Flowable<String>? = null

    fun get(): Flowable<String> {
        foregroundFlowable = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> getForegroundObservableHigherLollipop()
            else -> getForegroundObservableLowerLollipop()
        }

        return foregroundFlowable!!
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getForegroundObservableHigherLollipop(): Flowable<String> {
        return Flowable.interval(100, TimeUnit.MILLISECONDS)
            .filter { PermissionChecker.checkUsageAccessPermission(context) }
            .map {
                var usageEvent: UsageEvents.Event? = null

                val mUsageStatsManager = context.getSystemService(Service.USAGE_STATS_SERVICE) as UsageStatsManager
                val time = System.currentTimeMillis()

                val usageEvents = mUsageStatsManager.queryEvents(time - 1000 * 3600, time)
                val event = UsageEvents.Event()
                while (usageEvents.hasNextEvent()) {
                    usageEvents.getNextEvent(event)
                    if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        usageEvent = event
                    }
                }
                UsageEventWrapper(usageEvent)
            }
            .filter { it.usageEvent != null }
            .map { it.usageEvent }
            .filter { it.className != null }
            .filter { it.className.contains(OverlayValidationActivity::class.java.simpleName).not() }
            .map { it.packageName }
            .distinctUntilChanged()
    }

    private fun getForegroundObservableLowerLollipop(): Flowable<String> {
        return Flowable.interval(100, TimeUnit.MILLISECONDS)
            .map {
                val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                mActivityManager.getRunningTasks(1)[0].topActivity
            }
            .filter { it.className.contains(OverlayValidationActivity::class.java.simpleName).not() }
            .map { it.packageName }
            .distinctUntilChanged()
    }
}