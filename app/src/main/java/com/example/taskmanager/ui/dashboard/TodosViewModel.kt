package com.example.taskmanager.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.database.AppDatabase
import com.example.taskmanager.models.TodoModel

class TodosViewModel(application: Application) : AndroidViewModel(application) {


    private var appDatabase : AppDatabase
    init {
        appDatabase = AppDatabase.getDatabase(application)!!
    }

    val todos: LiveData<List<TodoModel>> = appDatabase.todoManagerDao().getAllToDos()
    fun todoComplete(todoId: String, complete: Boolean) {
        appDatabase.todoManagerDao().updateTodo(todoId,complete)
    }
}