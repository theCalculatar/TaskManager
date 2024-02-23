package com.example.taskmanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("TASK")
class TaskModel(
    @PrimaryKey(true)
    val id: Long?,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("status")
    val status: String,
    @ColumnInfo("description")
    val description: String?,
    @ColumnInfo("start date")
    val startDate: String?,
    @ColumnInfo("due date")
    val dueDate: String?,
    @ColumnInfo("priority")
    val priority: String?,
    )