package com.example.taskmanager.models

import android.icu.text.CaseMap.Title
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("TASK")
class TaskModel(
    @PrimaryKey(true)
    val id:Long,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("priority")
    val status: String,

    )