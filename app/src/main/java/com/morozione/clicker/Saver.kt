package com.morozione.clicker

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Saver @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataRecord: SharedPreferences = context.getSharedPreferences(KEY_RECORD, Context.MODE_PRIVATE)

    fun saveInt(value: Int, key: String) {
        dataRecord.edit {
            putInt(key, value)
        }
    }

    fun loadInt(key: String, defaultValue: Int = 0): Int =
        dataRecord.getInt(key, defaultValue)

    companion object {
        const val KEY_RECORD = "record"
        const val KEY_COINS = "coins"
        const val KEY_COUNT_MINUTES = "count_minutes"
    }
} 