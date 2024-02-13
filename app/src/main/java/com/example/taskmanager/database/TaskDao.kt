package com.example.taskmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel

@Dao
interface TaskDao {
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



}