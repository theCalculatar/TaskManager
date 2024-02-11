package com.example.taskmanager.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.adapter.TodoAdapter
import com.example.taskmanager.models.TodoModel

class TaskDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)
        //
        val title = findViewById<EditText>(R.id.title)
        val description = findViewById<EditText>(R.id.description)
        val deadline = findViewById<TextView>(R.id.set_deadline)
        val addTodo = findViewById<ImageView>(R.id.add_todo)
        val items = findViewById<TextView>(R.id.item_count)
        val todoRecycler = findViewById<RecyclerView>(R.id.to_do_recycler)

        // From homepage
        val taskId = intent.getLongExtra("taskId",-1)
        //
        todoRecycler.layoutManager = LinearLayoutManager(this)

        addTodo.setOnClickListener {
            val todoFragment = TodoFragment()
            todoFragment.arguments = bundleOf("taskId" to taskId)
            todoFragment.show(supportFragmentManager,TodoFragment.TAG)
        }

//        viewModel.getTask(taskId).observe(this){taskModel->
//            title.setText(taskModel.title,TextView.BufferType.EDITABLE)
//            description.setText(taskModel.description,TextView.BufferType.EDITABLE)
//        }
        val todos = ArrayList<TodoModel>()
        val adapter = TodoAdapter(todos)

        viewModel.getTodo(taskId).observe(this){
            todos.addAll(it)
            items.text = it.size.toString()
            todoRecycler.adapter = adapter
        }
        adapter.onCheck = { todoId, adapterPosition,complete->
            viewModel.todoChanged(todoId,complete)
            // throws illegal exception because the method is call while recycler is not finished updating
            try {
                adapter.notifyItemChanged(adapterPosition)
            }catch (_:IllegalStateException){}
        }
    }
}
//fun EditText.afterTextChange(){
//
//}
