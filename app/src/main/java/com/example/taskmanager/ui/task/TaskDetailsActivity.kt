package com.example.taskmanager.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.Constants
import com.example.taskmanager.R
import com.example.taskmanager.adapter.TodoAdapter
import com.example.taskmanager.models.TodoModel
import com.example.taskmanager.ui.pickerdate.DatePickerFragment
import kotlinx.coroutines.launch
import java.time.Month

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)
        //
        val title = findViewById<EditText>(R.id.title)
        val description = findViewById<EditText>(R.id.description)
        val setDueDate = findViewById<TextView>(R.id.set_deadline)
        val date = findViewById<TextView>(R.id.due_date)
        val addTodo = findViewById<ImageView>(R.id.add_todo)
        val items = findViewById<TextView>(R.id.item_count)
        val todoRecycler = findViewById<RecyclerView>(R.id.to_do_recycler)

        // From homepage
        val taskId = intent.getLongExtra("taskId", -1)
        //
        var listSize = 0
        todoRecycler.layoutManager = LinearLayoutManager(this)

        setDueDate.setOnClickListener {
            val datePicker = DatePickerFragment()
            datePicker.arguments = bundleOf("taskId" to taskId)
            datePicker.show(supportFragmentManager, "DatePicker")
        }

        addTodo.setOnClickListener {
            val todoFragment = TodoFragment()
            todoFragment.arguments = bundleOf("taskId" to taskId)
            todoFragment.show(supportFragmentManager, TodoFragment.TAG)
        }

        viewModel.getTask(taskId).observe(this) { taskModel ->

            title.setText(taskModel.title, TextView.BufferType.EDITABLE)
            description.setText(taskModel.description, TextView.BufferType.EDITABLE)

            taskModel.dueDate?.split(" ")?.let { dateParts->
                val month = Month.of(dateParts[1].toInt()).toString().substring(0..2)
                val dueDate = "${dateParts[0]} $month ${dateParts[2]}"
                date.setText(dueDate, TextView.BufferType.EDITABLE)
            }
        }


        // uses watcher to update title
        title.apply {
            this.afterTextChanged {
                this.setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_NEXT ->
                            lifecycleScope.launch{
                                viewModel.updateTitle(taskId,it)
                            }
                    }
                    false
                }
            }
        }

        // uses watcher to update description
        description.apply {
            afterTextChanged {
                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE ->
                            lifecycleScope.launch{
                                viewModel.updateDescription(taskId,it)
                            }
                    }
                    false }
            }
        }

        val todos = ArrayList<TodoModel>(0)
        val adapter = TodoAdapter(todos)
        todoRecycler.adapter = adapter

        viewModel.crudTodo.observe(this){ crudTodo->

            when(crudTodo.action){
                Constants.TODO_ADD-> {
                    todos.add(crudTodo.model!!)
                    adapter.notifyItemInserted(todos.size-1)
                    listSize++
                    items.text = listSize.toString()
                }
                Constants.TODO_DEL->{
                    todos.removeAt(crudTodo.position!!)
                    adapter.notifyItemRemoved(crudTodo.position)
                    listSize--
                    items.text = listSize.toString()
                }
                Constants.TODO_UPDATE->{
                    todos [crudTodo.position!!] = crudTodo.model!!
                    adapter.notifyItemChanged(crudTodo.position)
                }
            }
        }

        viewModel.getTodos(taskId).observe(this) {
            todos.addAll(it)
            items.text = it.size.toString()
            listSize = it.size
            adapter.notifyItemInserted(todos.size)
        }

        adapter.onCheck = { _, model ->
//            TODO()//use other attributes than id because it is unknown after it is saved to the databse
            viewModel.todoComplete(model.id!!,model.complete)
        }

        adapter.onItemLongClick = { menuId, position, todo ->
            when(menuId){
                R.id.delete->{
                    viewModel.deleteTodo(todo.id!!,position)
                }
                R.id.mark_complete->{
                    viewModel.todoComplete(todo.id!!, !todo.complete )
                }
                R.id.update->{
                    val todoFragment = TodoFragment()
                    todoFragment.arguments = bundleOf("taskId" to taskId,
                        "position" to position,
                        "todoId" to todo.id!!,
                        "action" to Constants.TODO_UPDATE)
                    todoFragment.show(supportFragmentManager,todoFragment.tag)
                }
            }
        }
    }
}

