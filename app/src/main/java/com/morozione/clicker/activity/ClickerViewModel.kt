package com.morozione.clicker.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morozione.clicker.LevelsManager
import com.morozione.clicker.Saver
import com.morozione.clicker.SecondsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClickerUiState(
    val clicks: Int = 0,
    val timeLeft: Int = 5,
    val nextLevelTarget: Int = 35,
    val canBuyExtraTime: Boolean = false,
    val isGameFinished: Boolean = false
)

@HiltViewModel
class ClickerViewModel @Inject constructor(
    private val saver: Saver,
    private val secondsManager: SecondsManager,
    private val levelsManager: LevelsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClickerUiState())
    val uiState: StateFlow<ClickerUiState> = _uiState

    private var coins = 0
    private var isTimerStarted = false

    init {
        coins = saver.loadInt(Saver.KEY_COINS)
        updateCanBuyExtraTime()
    }

    fun onClickButton() {
        if (!isTimerStarted) {
            startTimer()
            isTimerStarted = true
        }

        val newClicks = _uiState.value.clicks + 1
        _uiState.update { it.copy(clicks = newClicks) }

        checkLevelProgress()
    }

    private fun startTimer() {
        viewModelScope.launch {
            secondsManager.startCountdown(viewModelScope)
                .collect { seconds ->
                    _uiState.update {
                        it.copy(
                            timeLeft = seconds,
                            isGameFinished = seconds == 0
                        )
                    }
                    if (seconds == 0) {
                        finishGame()
                    }
                }
        }
    }

    private fun checkLevelProgress() {
        val currentClicks = _uiState.value.clicks
        if (currentClicks >= levelsManager.getStartRecord()) {
            levelsManager.upperLevel()
            secondsManager.upperSecondsOnNewLevel()
            _uiState.update {
                it.copy(
                    nextLevelTarget = levelsManager.getStartRecord()
                )
            }
        }
    }

    fun onBuyExtraTime() {
        if (coins >= 30) {
            coins -= 30
            saver.saveInt(coins, Saver.KEY_COINS)
            secondsManager.upperSecondsOnNewLevel()
            updateCanBuyExtraTime()
        }
    }

    private fun updateCanBuyExtraTime() {
        _uiState.update {
            it.copy(
                canBuyExtraTime = coins >= 30
            )
        }
    }

    private fun finishGame() {
        val coinsEarned = _uiState.value.clicks
        coins += coinsEarned
        saver.saveInt(coins, Saver.KEY_COINS)
    }

    override fun onCleared() {
        super.onCleared()
        secondsManager.cancelCountdown()
    }
} 