package com.example.taskmanager.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmanager.Constants
import com.example.taskmanager.database.AppDatabase
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

class TaskViewModel(application: Application): AndroidViewModel(application) {
    private var appDatabase :AppDatabase
    init {
        appDatabase = AppDatabase.getDatabase(application)!!
    }

    private val _date = MutableLiveData<LocalDateTime?>()
    private val  _todos = MutableLiveData<List<TodoModel>>().apply {
        value = appDatabase.todoManagerDao().getAllToDos()
    }

    val date: LiveData<LocalDateTime?> = _date
    val crudTodo = MutableLiveData<CrudTodo>()
    val todos: LiveData<List<TodoModel>> = _todos
    val allTask:LiveData<List<TaskModel>> = appDatabase.taskManagerDao().getAllTasks()

    fun addTask(task: TaskModel) {
        appDatabase.taskManagerDao().insertTask(task)
    }

    fun getTodos(taskId: Long) :LiveData<List<TodoModel>>{
        val mutableTodos = MutableLiveData<List<TodoModel>>()
        mutableTodos.postValue(appDatabase.todoManagerDao().getToDos(taskId))
        return mutableTodos
    }

    fun getTodo(todoId: String): MutableLiveData<TodoModel> {
        val mutableLiveData = MutableLiveData<TodoModel>()
        mutableLiveData.postValue(appDatabase.todoManagerDao().getToDo(todoId))
        return mutableLiveData
    }


    fun getTask(taskId: Long):LiveData<TaskModel> = appDatabase.taskManagerDao().getTask(taskId)

    /**
     * Update task date
     * @param taskId  taskID of type long
     * @param dueDate  due date of type Date().toString()
     */
    fun addDate(taskId: Long, dueDate: String){
        val now = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = formatter.format(now)
        _date.postValue(null)
        appDatabase.taskManagerDao().updateDate(taskId, startDate.toString(), dueDate)
    }

    /**
     * Add date Locally to communicate two fragments
     * @param dueDate  due date of type Date().toString()
     */
    fun addDate(dueDate: LocalDateTime){
        _date.postValue(dueDate)
    }


    fun updateTitle(taskId: Long, title: String){
        appDatabase.taskManagerDao().updateTitle(taskId,title)
    }

    fun updateDescription(taskId: Long, title: String){
        appDatabase.taskManagerDao().updateDescription(taskId,title)
    }

    fun todoComplete(todoId: String, complete: Boolean) {
        appDatabase.todoManagerDao().updateTodo(todoId,complete)
    }

    /**
     * CRUD to-do model,
     * this function gives model and instructions
     */
    fun addTodo(todo: TodoModel){
        _date.value = null
        crudTodo.postValue(CrudTodo(null,Constants.TODO_ADD,todo))
        appDatabase.todoManagerDao().insertTodo(todo)
    }

    fun deleteTodo(todoId: String,position: Int){
        crudTodo.postValue(CrudTodo(position,Constants.TODO_DEL,null))
        appDatabase.todoManagerDao().deleteTodo(todoId)
    }

    fun updateTodo(todo: TodoModel, position: Int){
        _date.value = null
        crudTodo.postValue(CrudTodo(position,Constants.TODO_UPDATE,todo))
        appDatabase.todoManagerDao().updateTodo(todo)
    }

    fun updateTask(model: TaskModel) {
        appDatabase.taskManagerDao().updateTask(model)
    }

    fun deleteTask(taskId:Long){
        appDatabase.taskManagerDao().deleteTask(taskId)
        appDatabase.todoManagerDao().deleteTodos(taskId)
    }

    class CrudTodo(
        val position: Int?,
        val action:Int,
        val model: TodoModel?
    )
}