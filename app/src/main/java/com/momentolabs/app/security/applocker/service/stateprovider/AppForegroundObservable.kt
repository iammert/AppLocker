package com.momentolabs.app.security.applocker.service.stateprovider

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.momentolabs.app.security.applocker.ui.overlay.activity.OverlayValidationActivity
import com.momentolabs.app.security.applocker.ui.permissions.PermissionChecker
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Emits the package name of the app in the foreground whenever it changes.
 *
 * Usage events are polled incrementally: only the first query looks back
 * [INITIAL_LOOKBACK_MILLIS] to find the current app, later queries start at the last event
 * that was seen.
 */
class AppForegroundObservable @Inject constructor(val context: Context) {

    private val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    fun get(): Flowable<String> = Flowable.defer {
        var queryStartTime = System.currentTimeMillis() - INITIAL_LOOKBACK_MILLIS
        Flowable.interval(0, POLL_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
            .filter { PermissionChecker.checkUsageAccessPermission(context) }
            .concatMapIterable {
                val now = System.currentTimeMillis()
                val usageEvents = usageStatsManager.queryEvents(queryStartTime, now)
                val event = UsageEvents.Event()
                var foregroundPackage: String? = null
                while (usageEvents.hasNextEvent()) {
                    usageEvents.getNextEvent(event)
                    // Events can be written with a small delay, so the next query starts at the
                    // last event instead of now. Repeated events are dropped by distinctUntilChanged.
                    queryStartTime = event.timeStamp
                    if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND && isLockScreen(event).not()) {
                        foregroundPackage = event.packageName
                    }
                }
                listOfNotNull(foregroundPackage)
            }
            .distinctUntilChanged()
    }

    private fun isLockScreen(event: UsageEvents.Event): Boolean =
        event.className?.contains(OverlayValidationActivity::class.java.simpleName) == true

    companion object {
        private const val POLL_INTERVAL_MILLIS = 100L
        private val INITIAL_LOOKBACK_MILLIS = TimeUnit.HOURS.toMillis(1)
    }
}
