package com.example.taskmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel

@Dao
interface CardDao {
    @Query("SELECT * FROM task WHERE `id`=:taskId")
    fun getTask(taskId:Long): TaskModel

    @Query("SELECT * FROM task")
    fun getAllTasks():LiveData<List<TaskModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task:TaskModel)

    @Query("DELETE FROM task WHERE `id` = :taskId")
    fun deleteTask(taskId: Long)

    @Query("Update task SET title=:title, description=:description, `start date`=:startDate," +
            " `due date`=:endDate, `priority`=:priority WHERE id = :taskId")
    fun updateTask(taskId:Long, title:String, description:String, startDate:String,
                   endDate:String, priority: String)

    /**
     * To do dao
     */
    @Query("SELECT * FROM todo WHERE id=:taskId")
    fun getToDos(taskId: Long):LiveData<List<TodoModel>>

    @Query("SELECT * FROM todo WHERE id=:taskId")
    fun getAllToDos(taskId: Long):LiveData<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(todo:TodoModel)

    @Query("DELETE FROM task WHERE `id`=:todoId")
    fun deleteTodo(todoId: Long)

    @Query("Update todo SET title=:title, description=:description, " +
            "`complete`=:complete, `dueDate`=:dueDate WHERE id = :todoId")
    fun updateTodo(todoId:Long, title:String, description:String, complete:Boolean,
                   dueDate:String, )

}