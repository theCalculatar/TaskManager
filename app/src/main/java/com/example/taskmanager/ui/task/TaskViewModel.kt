package com.example.taskmanager.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.Constants
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel

class TaskViewModel():ViewModel() {
    fun addTask(task: TaskModel) {

    }

    fun getTodo(taskId: Long): LiveData<ArrayList<TodoModel>> {
        val todos = MutableLiveData<ArrayList<TodoModel>>()
        todos.postValue(arrayListOf(
            TodoModel(2L,"Lee","this is the" +
                    "\n description is kind long lol\n lool",true,"02/02/2024",),
            TodoModel(2L,"Lee","this is the description",true,"02/02/2024",),
            TodoModel(2L,"Lee","this is the description",false,"02/02/2024",),
            ))
        return todos
    }

    fun todoChanged(todoId: Long, complete: Boolean) {

    }


//
//    fun getTask(taskId: String): LiveData<TaskModel> {
//
//    }
}