package com.example.taskmanager.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.Constants
import com.example.taskmanager.database.AppDatabase
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel

class TaskViewModel(application: Application): AndroidViewModel(application) {

    private var appDatabase :AppDatabase
    init {
        appDatabase = AppDatabase.getDatabase(application)!!
    }

    fun addTask(task: TaskModel) {
        appDatabase.taskManagerDao().insertTask(task)
    }

    fun getTodo(taskId: Long) = appDatabase.taskManagerDao().getToDos(taskId)

//
//            LiveData<ArrayList<TodoModel>> {
//        val todos = MutableLiveData<ArrayList<TodoModel>>()
//        todos.postValue(arrayListOf(
//            TodoModel(2L,"Go to a meeting","this is the" +
//                    "\n description is kind long lol\n lool",true,"02/02/2024",),
//            TodoModel(2L,"Finish Project task manager","this is the description",true,"02/02/2024",),
//            TodoModel(2L,"Lee","this is the description",false,"02/02/2024",),
//            ))
//        return todos
//    }

    fun todoChanged(todoId: Long, complete: Boolean) {

    }

    fun getTask(taskId: Long): Any {
        TODO("Not yet implemented")
    }


//
//    fun getTask(taskId: String): LiveData<TaskModel> {
//
//    }
}