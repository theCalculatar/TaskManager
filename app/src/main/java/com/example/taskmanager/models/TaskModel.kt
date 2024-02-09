package com.example.taskmanager.models

import android.icu.text.CaseMap.Title
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("TASK")
class TaskModel(
    @PrimaryKey(true)
    val id:Long,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("priority")
    val status: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("start date")
    val startDate: String,
    @ColumnInfo("end date")
    val endDate: String,
    )