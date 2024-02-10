package com.example.taskmanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("todo")
class TodoModel(
    @PrimaryKey(true)
    val id:Long?,
    val title:String,
    val description:String?,
    val complete:Boolean=false,
    val dueDate:String) {

}
