package com.example.taskmanager.ui.alarmManager

import java.time.LocalDateTime

data class AlarmItem(
    val alarmTime: LocalDateTime?,
    val message: String?,
    val itemId:Long
)