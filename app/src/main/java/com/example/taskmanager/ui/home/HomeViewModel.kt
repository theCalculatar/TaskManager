package com.example.taskmanager.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.Constants
import com.example.taskmanager.database.AppDatabase
import com.example.taskmanager.models.TaskModel

class HomeViewModel (application: Application): AndroidViewModel(application) {

    private var appDatabase : AppDatabase
    init {
        appDatabase = AppDatabase.getDatabase(application)!!
    }

    val tasks: LiveData<List<TaskModel>> = appDatabase.taskManagerDao().getAllTasks()

}