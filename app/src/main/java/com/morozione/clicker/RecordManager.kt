package com.morozione.clicker

import androidx.compose.ui.graphics.Color

data class RecordFeedback(
    val message: String,
    val color: Color
)

object RecordManager {
    fun getRecordFeedback(record: Int, newResult: Int): RecordFeedback {
        return when {
            newResult > record -> RecordFeedback(
                message = "New Record!!!",
                color = Color(0xFF3AA63A)
            )
            newResult > (record * 3 / 4) -> RecordFeedback(
                message = "Not Bad",
                color = Color(0xFF989608)
            )
            newResult > (record / 2) -> RecordFeedback(
                message = "So-So",
                color = Color(0xFF989608)
            )
            newResult > (record / 4) -> RecordFeedback(
                message = "Bad",
                color = Color(0xFFB01111)
            )
            else -> RecordFeedback(
                message = "You Loser!!",
                color = Color(0xFFB01111)
            )
        }
    }
} 