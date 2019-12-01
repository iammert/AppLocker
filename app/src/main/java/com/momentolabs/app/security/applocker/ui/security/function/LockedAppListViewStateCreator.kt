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
        val itemViewStateList: ArrayList<AppLockItemItemViewState> = arrayListOf()

        installedApps.forEach { installedApp ->
            val itemViewState =
                AppLockItemItemViewState(installedApp)

            lockedApps.forEach { lockedApp ->
                if (installedApp.packageName == lockedApp.packageName) {
                    itemViewState.isLocked = true
                }
            }

            itemViewStateList.add(itemViewState)
        }

        return itemViewStateList
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