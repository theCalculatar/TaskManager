package com.example.taskmanager.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmanager.Constants
import com.example.taskmanager.database.AppDatabase
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel

class TaskViewModel(application: Application): AndroidViewModel(application) {

    private var appDatabase :AppDatabase
    val crudTodo = MutableLiveData<CrudTodo>()
    init {
        appDatabase = AppDatabase.getDatabase(application)!!
    }

    fun addTask(task: TaskModel) {
        appDatabase.taskManagerDao().insertTask(task)
    }

    fun getTodos(taskId: Long) :LiveData<List<TodoModel>>{
        val mutableTodos = MutableLiveData<List<TodoModel>>()
        mutableTodos.postValue(appDatabase.todoManagerDao().getToDos(taskId))
        return mutableTodos
    }
    fun getTodo(todoId: Long): MutableLiveData<TodoModel> {

        val mutableLiveData = MutableLiveData<TodoModel>()
        mutableLiveData.postValue(appDatabase.todoManagerDao().getToDo(todoId))
        return mutableLiveData
    }


        fun getTask(taskId: Long):LiveData<TaskModel> = appDatabase.taskManagerDao().getTask(taskId)

    fun addDates(taskId: Long,startDate: String,endDate: String){
        appDatabase.taskManagerDao().updateDate(taskId, startDate, endDate)
    }

    fun updateTitle(taskId: Long, title: String){
        appDatabase.taskManagerDao().updateTitle(taskId,title)
    }

    fun updateDescription(taskId: Long, title: String){
        appDatabase.taskManagerDao().updateDescription(taskId,title)
    }


    val allTask:LiveData<List<TaskModel>> = appDatabase.taskManagerDao().getAllTasks()

    fun todoComplete(todoId: Long, complete: Boolean) {
        appDatabase.todoManagerDao().updateTodo(todoId,complete)
    }

    /**
     * CRUD todo model
     */
    fun addTodo(todo: TodoModel){
        crudTodo.postValue(CrudTodo(null,Constants.TODO_ADD,todo))
        appDatabase.todoManagerDao().insertTodo(todo)
    }

    fun deleteTodo(todoId: Long,position: Int){
        crudTodo.postValue(CrudTodo(position,Constants.TODO_DEL,null))
        appDatabase.todoManagerDao().deleteTodo(todoId)
    }

    fun updateTodo(todo: TodoModel, position: Int){
        crudTodo.postValue(CrudTodo(position,Constants.TODO_UPDATE,todo))
        appDatabase.todoManagerDao().updateTodo(todo)
    }


    class CrudTodo(
        val position: Int?,
        val action:Int,
        val model: TodoModel?
    )
}