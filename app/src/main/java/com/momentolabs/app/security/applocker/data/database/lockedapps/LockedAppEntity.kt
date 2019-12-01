package com.momentolabs.app.security.applocker.data.database.lockedapps

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locked_app")
data class LockedAppEntity(@PrimaryKey @ColumnInfo(name = "packageName") val packageName: String) {

    fun parsePackageName() : String{
        return packageName.substring(0, packageName.indexOf("/"))
    }
}