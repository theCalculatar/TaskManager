package com.example.taskmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.ui.task.FragmentCreateTask
import com.example.taskmanager.ui.task.TodoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration:AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val collapseToolbar = binding.toolbarLayout
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_task)
        )

        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // react on change
            // you can check destination.id or destination.label and act based on that
            when(destination.id ==R.id.navigation_home){
                true->{
                    navController.popBackStack(R.id.navigation_task,true)
                }
                false->{}
            }
            collapseToolbar.title = destination.label.toString()
        }
        binding.fab.setOnClickListener {
            when(navController.currentDestination?.id){
                R.id.navigation_home->{
                    FragmentCreateTask().show(supportFragmentManager, FragmentCreateTask.TAG)
                }
                else ->{
                    TodoFragment().show(supportFragmentManager, TodoFragment.TAG)
                }
            }
        }
        //To show the notification, we need to create a notification channel first.
        val taskChannelId = "TASK_ID"
        val todoChannelId = "TODO_ID"
        val taskChannelName = "Task Alarm"
        val todoChannelName = "Todo Alarm"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val taskChannel = NotificationChannel(
            taskChannelId,
            taskChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        val todoChannel = NotificationChannel(
            todoChannelId,
            todoChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(taskChannel)
        notificationManager.createNotificationChannel(todoChannel)
    }
}