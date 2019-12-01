package com.momentolabs.app.security.applocker.data

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppEntity
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppsDao
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class AppDataProvider @Inject constructor(
    val context: Context,
    val appsDao: LockedAppsDao
) {

    fun fetchInstalledAppList(): Single<List<AppData>> {
        return Single.create {
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val resolveInfoList: List<ResolveInfo> = context.packageManager.queryIntentActivities(mainIntent, 0)

            val appDataList: ArrayList<AppData> = arrayListOf()
            resolveInfoList.forEach { resolveInfo ->
                with(resolveInfo) {
                    if (activityInfo.packageName != context.packageName) {
                        val mainActivityName = activityInfo.name.substring(activityInfo.name.lastIndexOf(".") + 1)
                        val appData = AppData(
                            appName = loadLabel(context.packageManager) as String,
                            packageName = "${activityInfo.packageName}/$mainActivityName",
                            appIconDrawable = loadIcon(context.packageManager)
                        )
                        appDataList.add(appData)
                    }
                }
            }

            val lockedAppList = appsDao.getLockedAppsSync()

            val orderedList = orderAppsByLockStatus(appDataList, lockedAppList)

            it.onSuccess(orderedList)
        }
    }

    private fun orderAppsByLockStatus(allApps: List<AppData>, lockedApps: List<LockedAppEntity>): List<AppData> {
        val resultList = arrayListOf<AppData>()


        lockedApps.forEach { lockedAppEntity ->
            allApps.forEach { appData ->
                if (lockedAppEntity.packageName == appData.packageName) {
                    resultList.add(appData)
                }
            }
        }


        val alphabeticOrderList: ArrayList<AppData> = arrayListOf()

        allApps.forEach { appData ->
            if (resultList.contains(appData).not()) {
                alphabeticOrderList.add(appData)
            }
        }
        alphabeticOrderList.sortWith(Comparator { app1, app2 -> app1.appName.compareTo(app2.appName) })
        resultList.addAll(alphabeticOrderList)

        return resultList
    }

}