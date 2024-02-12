package com.example.taskmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel

@Database(entities =
[TaskModel::class,TodoModel::class], exportSchema = false, version = 2,)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskManagerDao(): CardDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            // if no Migration object.
                            AppDatabase::class.java, "task_database"
                        ) // Wipes and rebuilds instead of migrating
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}