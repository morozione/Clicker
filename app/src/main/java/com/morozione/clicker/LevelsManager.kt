package com.morozione.clicker

import javax.inject.Inject

class LevelsManager @Inject constructor() {
    private var level: Int = 1
    private var startRecord: Int = 35

    fun upperLevel() {
        level++
        startRecord += level * 20
    }

    fun getStartRecord(): Int = startRecord

    fun getLevel(): Int = level
} 