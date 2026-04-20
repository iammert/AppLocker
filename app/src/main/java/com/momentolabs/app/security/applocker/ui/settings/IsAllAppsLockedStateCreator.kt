package com.momentolabs.app.security.applocker.ui.settings

import com.momentolabs.app.security.applocker.data.AppData
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppEntity
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class IsAllAppsLockedStateCreator : BiFunction<List<AppData>, List<LockedAppEntity>, Boolean> {

    /**
     * Compares package names instead of list sizes: the locked list can still contain apps that
     * were uninstalled, which made the sizes match while some installed apps were unlocked.
     */
    override fun apply(installedApps: List<AppData>, lockedApps: List<LockedAppEntity>): Boolean {
        if (installedApps.isEmpty()) {
            return false
        }
        val lockedPackages = lockedApps.mapTo(HashSet()) { it.packageName }
        return installedApps.all { it.packageName in lockedPackages }
    }

    companion object {

        fun create(
            appDataListObservable: Observable<List<AppData>>,
            lockedAppsObservable: Observable<List<LockedAppEntity>>
        ): Observable<Boolean> {
            return Observable.combineLatest(appDataListObservable, lockedAppsObservable, IsAllAppsLockedStateCreator())
        }
    }

}