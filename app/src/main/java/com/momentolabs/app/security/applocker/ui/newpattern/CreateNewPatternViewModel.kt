package com.momentolabs.app.security.applocker.ui.newpattern

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrognito.patternlockview.PatternLockView
import com.momentolabs.app.security.applocker.util.extensions.convertToPatternDot
import com.momentolabs.app.security.applocker.data.database.pattern.PatternDao
import com.momentolabs.app.security.applocker.data.database.pattern.PatternDotMetadata
import com.momentolabs.app.security.applocker.data.database.pattern.PatternEntity
import com.momentolabs.app.security.applocker.ui.RxAwareViewModel
import com.momentolabs.app.security.applocker.util.extensions.doOnBackground
import com.momentolabs.app.security.applocker.util.helper.PatternChecker
import javax.inject.Inject

class CreateNewPatternViewModel @Inject constructor(val patternDao: PatternDao) : RxAwareViewModel() {

    enum class PatternEvent {
        INITIALIZE, FIRST_COMPLETED, SECOND_COMPLETED, ERROR
    }

    private val patternEventLiveData = MutableLiveData<CreateNewPatternViewState>().apply {
        value = CreateNewPatternViewState(PatternEvent.INITIALIZE)
    }

    private var firstDrawedPattern: ArrayList<PatternLockView.Dot> = arrayListOf()
    private var redrawedPattern: ArrayList<PatternLockView.Dot> = arrayListOf()

    fun getPatternEventLiveData(): LiveData<CreateNewPatternViewState> = patternEventLiveData

    fun setFirstDrawedPattern(pattern: List<PatternLockView.Dot>?) {
        pattern?.let {
            this.firstDrawedPattern.clear()
            this.firstDrawedPattern.addAll(pattern)
            patternEventLiveData.value = CreateNewPatternViewState(PatternEvent.FIRST_COMPLETED)
        }
    }

    fun setRedrawnPattern(pattern: List<PatternLockView.Dot>?) {
        pattern?.let {
            this.redrawedPattern.clear()
            this.redrawedPattern.addAll(pattern)
            if (PatternChecker.checkPatternsEqual(
                    firstDrawedPattern.convertToPatternDot(),
                    redrawedPattern.convertToPatternDot()
                )
            ) {
                saveNewCreatedPattern(firstDrawedPattern)
                patternEventLiveData.value = CreateNewPatternViewState(PatternEvent.SECOND_COMPLETED)
            } else {
                firstDrawedPattern.clear()
                redrawedPattern.clear()
                patternEventLiveData.value = CreateNewPatternViewState(PatternEvent.ERROR)
            }
        }
    }

    fun isFirstPattern(): Boolean = firstDrawedPattern.isEmpty()

    private fun saveNewCreatedPattern(pattern: List<PatternLockView.Dot>){
        doOnBackground {
            val patternMetadata = PatternDotMetadata(pattern.convertToPatternDot())
            val patternEntity = PatternEntity(patternMetadata)
            patternDao.createPattern(patternEntity)
        }
    }
}