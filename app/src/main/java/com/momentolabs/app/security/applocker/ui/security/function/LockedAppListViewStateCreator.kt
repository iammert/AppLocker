package com.momentolabs.app.security.applocker.ui.security.function

import com.momentolabs.app.security.applocker.data.AppData
import com.momentolabs.app.security.applocker.data.database.lockedapps.LockedAppEntity
import com.momentolabs.app.security.applocker.ui.security.AppLockItemItemViewState
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class LockedAppListViewStateCreator : BiFunction<List<AppData>, List<LockedAppEntity>, List<AppLockItemItemViewState>> {

    override fun apply(
        installedApps: List<AppData>,
        lockedApps: List<LockedAppEntity>
    ): List<AppLockItemItemViewState> {
        val lockedPackages = lockedApps.mapTo(HashSet()) { it.packageName }
        return installedApps.map { AppLockItemItemViewState(it, isLocked = it.packageName in lockedPackages) }
    }

    companion object {

        fun create(
            appDataListObservable: Observable<List<AppData>>,
            lockedAppsObservable: Observable<List<LockedAppEntity>>
        ): Observable<List<AppLockItemItemViewState>> {
            return Observable.combineLatest(
                appDataListObservable, lockedAppsObservable,
                LockedAppListViewStateCreator()
            )
        }
    }

}