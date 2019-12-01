package com.momentolabs.app.security.applocker.repository

import com.momentolabs.app.security.applocker.data.database.callblocker.blacklist.BlackListDao
import com.momentolabs.app.security.applocker.data.database.callblocker.blacklist.BlackListItemEntity
import com.momentolabs.app.security.applocker.data.database.callblocker.calllog.CallLogDao
import com.momentolabs.app.security.applocker.data.database.callblocker.calllog.CallLogItemEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class CallBlockerRepository @Inject constructor(val blackListDao: BlackListDao, val callLogDao: CallLogDao) {

    fun getBlackList(): Flowable<List<BlackListItemEntity>> {
        return blackListDao.getBlackList()
    }

    fun addToBlackList(blackListItemEntity: BlackListItemEntity): Completable {
        return Completable.create {
            blackListDao.addToBlacklist(blackListItemEntity)
            it.onComplete()
        }
    }

    fun deleteBlackListItem(blackListId: Int): Completable {
        return Completable.create {
            blackListDao.delete(blackListId)
            it.onComplete()
        }
    }

    fun getCallLogs(): Flowable<List<CallLogItemEntity>> {
        return callLogDao.getCallLogs()
    }

    fun addToCallLog(callLogItemEntity: CallLogItemEntity): Completable {
        return Completable.create {
            callLogDao.addToLog(callLogItemEntity)
            it.onComplete()
        }
    }

    fun deleteCallLog(callLogId: Int): Completable {
        return Completable.create {
            callLogDao.delete(callLogId)
            it.onComplete()
        }
    }
}