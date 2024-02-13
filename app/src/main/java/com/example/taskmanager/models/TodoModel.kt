package com.example.taskmanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("todo")
class TodoModel(
    @PrimaryKey(true)
    val id:Long?,
    val taskId:Long?,
    val title:String,
    val description:String?,
    var complete:Boolean=false,
    val dueDate:String?) {

}
