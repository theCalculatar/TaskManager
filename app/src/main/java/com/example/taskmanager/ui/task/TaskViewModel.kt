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

    fun getTodo(taskId: Long) :LiveData<List<TodoModel>> = appDatabase.taskManagerDao().getToDos(taskId)

    fun getTask(taskId: Long) = appDatabase.taskManagerDao().getTask(taskId)

    val allTask:LiveData<List<TaskModel>> = appDatabase.taskManagerDao().getAllTasks()

    fun todoComplete(todoId: Long, complete: Boolean) {
        appDatabase.taskManagerDao().updateTodo(todoId,complete)
    }

    fun addTodo(todo: TodoModel){
        appDatabase.taskManagerDao().insertTodo(todo)
    }
//    fun todoChanged(todoModel: TodoModel) {
//        appDatabase.taskManagerDao().updateTodo(todoModel)
//    }

//    fun getTask(taskId: Long) = appDatabase.taskManagerDao().getTask(taskId)


//
//    fun getTask(taskId: String): LiveData<TaskModel> {
//
//    }
}