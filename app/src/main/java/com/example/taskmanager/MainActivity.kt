package com.example.taskmanager

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.ui.task.FragmentCreateTask
import com.example.taskmanager.ui.task.TodoFragment

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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // react on change
            // you can check destination.id or destination.label and act based on that
            when(destination.id ==R.id.navigation_home){
                true->{
                    navController.popBackStack(R.id.navigation_task,false)
                }
                false->{}
            }
            collapseToolbar.title = destination.label.toString()
        }
        navView.setupWithNavController(navController)
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
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}