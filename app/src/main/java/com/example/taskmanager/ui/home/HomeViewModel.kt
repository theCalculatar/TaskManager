package com.example.taskmanager.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.taskmanager.database.AppDatabase
import com.example.taskmanager.models.TaskModel

class HomeViewModel (application: Application): AndroidViewModel(application) {

    private var appDatabase : AppDatabase
    init {
        appDatabase = AppDatabase.getDatabase(application)!!
    }

    val tasks: LiveData<List<TaskModel>> = appDatabase.taskManagerDao().getAllTasks()

    fun deleteTask(taskId:Long){
        appDatabase.taskManagerDao().deleteTask(taskId)
        appDatabase.todoManagerDao().deleteTodos(taskId)
    }

    fun markComplete(task:TaskModel){
        appDatabase.taskManagerDao().updateTask(task)
    }
}