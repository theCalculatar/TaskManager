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

//    private val _tasks = MutableLiveData<ArrayList<TaskModel>>().apply {
//        value = arrayListOf(
//            TaskModel(2L,"Lee","complete","this is the description","02/02/2024","02/08/2024",""),
//            TaskModel(2L,"Lee","complete","this is the description","02/02/2024","02/09/2024",""),
//            TaskModel(2L,"Lee",Constants.TASK_STATUS_COMPLETE,"this is the description","02/02/2024","02/12/2024",""),
//            TaskModel(2L,"Lee","complete","this is the description","02/07/2024","02/10/2024",""))
//    }
    val tasks: LiveData<List<TaskModel>> = appDatabase.taskManagerDao().getAllTasks()

}