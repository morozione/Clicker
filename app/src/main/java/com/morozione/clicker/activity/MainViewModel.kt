package com.morozione.clicker.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morozione.clicker.Saver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val record: Int = 0,
    val coins: Int = 0,
    val newRecord: Int = 0,
    val isPlaying: Boolean = false,
    val showRecordDialog: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saver: Saver
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        val record = saver.loadInt(Saver.KEY_RECORD)
        val coins = saver.loadInt(Saver.KEY_COINS)
        _uiState.update { it.copy(record = record, coins = coins) }
    }

    fun onStartClick() {
        _uiState.update { it.copy(isPlaying = true) }
        // Navigate to ClickerActivity will be handled by the Activity
    }

    fun onGameFinished(newRecord: Int, earnedCoins: Int) {
        _uiState.update { 
            it.copy(
                newRecord = newRecord,
                showRecordDialog = true,
                isPlaying = false
            )
        }
        if (newRecord > uiState.value.record) {
            updateRecord(newRecord, earnedCoins)
        }
    }

    private fun updateRecord(newRecord: Int, earnedCoins: Int) {
        viewModelScope.launch {
            saver.saveInt(newRecord, Saver.KEY_RECORD)
            val updatedCoins = uiState.value.coins + earnedCoins
            saver.saveInt(updatedCoins, Saver.KEY_COINS)
            _uiState.update {
                it.copy(
                    record = newRecord,
                    coins = updatedCoins
                )
            }
        }
    }

    fun onRecordDialogDismiss() {
        _uiState.update { it.copy(showRecordDialog = false) }
    }
} 