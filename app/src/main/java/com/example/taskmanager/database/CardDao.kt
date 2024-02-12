package com.example.taskmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel

@Dao
interface CardDao {
    @Query("SELECT * FROM task WHERE `id`=:taskId")
    fun getTask(taskId:Long): LiveData<TaskModel>

    @Query("SELECT * FROM task")
    fun getAllTasks():LiveData<List<TaskModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task:TaskModel)

    @Query("DELETE FROM task WHERE `id` = :taskId")
    fun deleteTask(taskId: Long)

    @Query("UPDATE task SET `start date`=:startDate," +
            " `due date`=:endDate WHERE id = :taskId")
    fun updateTask(taskId:Long,  startDate:String,
                   endDate:String)

    @Query("UPDATE task SET title=:title, description=:description,`priority`=:priority WHERE id = :taskId")
    fun updateTask(taskId:Long, title:String, description:String, priority: String)


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

    /**
     * Minor update if only
     * todo is complete
     */
    @Query("Update todo SET `complete`=:complete WHERE id=:todoId")
    fun updateTodo(todoId: Long, complete: Boolean)

    /**
     * Updates everything
     */
    @Update
    fun updateTodo(todo: TodoModel)

}