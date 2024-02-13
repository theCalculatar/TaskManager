package com.example.taskmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel

/**
 * To do dao
 */
@Dao
interface TodoDao {

    @Query("SELECT * FROM todo WHERE taskId=:taskId")
    fun getToDos(taskId: Long):List<TodoModel>

    @Query("SELECT * FROM todo WHERE id=:todoId")
    fun getToDo(todoId: Long):TodoModel

    @Query("SELECT * FROM todo WHERE taskId=:taskId")
    fun getAllToDos(taskId: Long):LiveData<List<TodoModel>>

    @Query("SELECT * FROM todo")
    fun getAllToDos():LiveData<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(todo:TodoModel)

    @Query("DELETE FROM todo WHERE `id`=:todoId")
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