package com.momentolabs.app.security.applocker.data

import android.content.Context
import android.content.Intent
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppEntity
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppsDao
import io.reactivex.Single
import java.text.Collator
import javax.inject.Inject

class AppDataProvider @Inject constructor(
    val context: Context,
    val appsDao: LockedAppsDao
) {

    /**
     * Returns the launchable apps, locked apps first and the rest in alphabetical order.
     *
     * @param loadIcons icons take most of the time and memory, skip them when only the list of
     * apps is needed
     */
    fun fetchInstalledAppList(loadIcons: Boolean = true): Single<List<AppData>> {
        return Single.fromCallable {
            val packageManager = context.packageManager
            val mainIntent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
            val appDataList = packageManager.queryIntentActivities(mainIntent, 0)
                .filter { it.activityInfo.packageName != context.packageName }
                .map { resolveInfo ->
                    val activityInfo = resolveInfo.activityInfo
                    val mainActivityName = activityInfo.name.substringAfterLast('.')
                    AppData(
                        appName = resolveInfo.loadLabel(packageManager).toString(),
                        packageName = "${activityInfo.packageName}/$mainActivityName",
                        appIconDrawable = if (loadIcons) resolveInfo.loadIcon(packageManager) else null
                    )
                }

            orderAppsByLockStatus(appDataList, appsDao.getLockedAppsSync())
        }
    }

    private fun orderAppsByLockStatus(allApps: List<AppData>, lockedApps: List<LockedAppEntity>): List<AppData> {
        val lockedPackages = lockedApps.mapTo(HashSet()) { it.packageName }
        val (locked, unlocked) = allApps.partition { it.packageName in lockedPackages }
        val collator = Collator.getInstance()
        return locked.sortedWith(compareBy(collator) { it.appName }) +
                unlocked.sortedWith(compareBy(collator) { it.appName })
    }
}
