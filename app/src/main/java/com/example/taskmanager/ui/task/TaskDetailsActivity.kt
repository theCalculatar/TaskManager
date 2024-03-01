package com.example.taskmanager.ui.task


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.substring
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.Constants
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.example.taskmanager.adapter.TodoAdapter
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel
import com.example.taskmanager.ui.alarmManager.AlarmItem
import com.example.taskmanager.ui.pickerdate.DatePickerFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.collections.ArrayList

class TaskDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel
    private var fromReceiver = false
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
        val save = findViewById<FloatingActionButton>(R.id.save)
        val todoRecycler = findViewById<RecyclerView>(R.id.to_do_recycler)

        // From homepage or receiver
        val taskId = intent.getLongExtra("taskId", -1)
        fromReceiver = intent.getBooleanExtra("fromReceiver",false)
        if (taskId ==-1L) finish()
        //

        var listSize = 0
        todoRecycler.layoutManager = LinearLayoutManager(this)

        addTodo.setOnClickListener {
            val todoFragment = TodoFragment()
            todoFragment.arguments = bundleOf("taskId" to taskId)
            todoFragment.show(supportFragmentManager, TodoFragment.TAG)
        }

        //get task by id
        viewModel.getTask(taskId).observe(this) { taskModel ->
            title.setText(taskModel.title, TextView.BufferType.EDITABLE)
            description.setText(taskModel.description, TextView.BufferType.EDITABLE)

            taskModel.dueDate?.let {
                dateTime(it).let {
                    val month = it.month.toString().substring(0..2)
                    val dueDate = "${it.year} $month ${it.dayOfMonth}"
                    date.setText(dueDate, TextView.BufferType.EDITABLE)
                }
            }
            setDueDate.setOnClickListener {
                val datePicker = DatePickerFragment()
                datePicker.arguments = bundleOf("itemId" to taskId, "title" to taskModel.title)
                datePicker.show(supportFragmentManager, "DatePicker")
            }
            save.setOnClickListener {
                viewModel.updateTask(TaskModel(taskId,title.text.toString(),taskModel.status,
                description.text.toString(),taskModel.startDate,taskModel.dueDate, priority = taskModel.priority))
            }
        }

        // uses watcher to update title
        title.apply {
            this.afterTextChanged {
                save.isVisible = it.isNotBlank()

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
            save.isVisible = true
        }



        val todos = ArrayList<TodoModel>(0)
        val adapter = TodoAdapter(todos)
        todoRecycler.adapter = adapter

        viewModel.crudTodo.observe(this){ crudTodo->
            //using live data to update recycler view smooothly
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
            //mark model complete
            viewModel.todoComplete(model.id,model.complete)
        }

        adapter.onItemLongClick = { menuId, position, todo ->
            when(menuId){
                R.id.delete->{
                    viewModel.deleteTodo(todo.id,position)
                }
                R.id.mark_complete->{
                    viewModel.todoComplete(todo.id, !todo.complete )
                }
                R.id.update->{
                    val todoFragment = TodoFragment()
                    todoFragment.arguments = bundleOf("taskId" to taskId,
                        "position" to position,
                        "todoId" to todo.id,
                        "action" to Constants.TODO_UPDATE)
                    todoFragment.show(supportFragmentManager,todoFragment.tag)
                }
            }
        }
    }
    fun dateTime(date:String):LocalDateTime{
        val dateParts = date.split("-")
        val timeParts = dateParts[2].split("T")[1].split(":")

        val year = dateParts[0].toInt()
        val month = dateParts[1].toInt()
        val dayOfMonth = dateParts[2].split("T")[0].toInt()

        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        return LocalDateTime.of(year,month,dayOfMonth, hour, minute)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fromReceiver){
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
            finish()
        }
    }

}

