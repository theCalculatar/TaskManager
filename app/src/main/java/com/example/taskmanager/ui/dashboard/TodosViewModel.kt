package com.example.taskmanager.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.models.TodoModel

class TodosViewModel : ViewModel() {

    private val _text = MutableLiveData<ArrayList<TodoModel>>().apply {
        value = arrayListOf(
            TodoModel(2L,"Go to a meeting","this is the" +
                    "\n description is kind long lol\n lool",true,"02/02/2024",),
            TodoModel(2L,"Finish Project task manager","this is the description",true,"02/02/2024",),
            TodoModel(2L,"Lee","this is the description",false,"02/02/2024",),
        )
//        value = "This is dashboard Fragment"
    }
    fun getTodo(): LiveData<ArrayList<TodoModel>> {
        val todos = MutableLiveData<ArrayList<TodoModel>>()
        todos.postValue(arrayListOf(
            TodoModel(2L,"Go to a meeting","this is the" +
                    "\n description is kind long lol\n lool",true,"02/02/2024",),
            TodoModel(2L,"Finish Project task manager","this is the description",true,"02/02/2024",),
            TodoModel(2L,"Lee","this is the description",false,"02/02/2024",),
        ))
        return todos
    }

    val todos: LiveData<ArrayList<TodoModel>> = _text
    fun todoChanged(todoId: Long, complete: Boolean) {

    }
}