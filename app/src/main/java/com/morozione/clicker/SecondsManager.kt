package com.morozione.clicker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SecondsManager @Inject constructor() {
    private var seconds = 5
    private var countdownJob: Job? = null

    fun startCountdown(scope: CoroutineScope): Flow<Int> = flow {
        while (seconds >= 0) {
            emit(seconds--)
            delay(1000) // 1 second delay
        }
    }.flowOn(Dispatchers.Default)

    fun upperSecondsOnNewLevel() {
        seconds += 5
    }

    fun cancelCountdown() {
        countdownJob?.cancel()
    }
} 