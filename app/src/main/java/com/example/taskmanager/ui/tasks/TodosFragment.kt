package com.example.taskmanager.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.Constants
import com.example.taskmanager.R
import com.example.taskmanager.adapter.TodoAdapter
import com.example.taskmanager.databinding.FragmentTaskBinding
import com.example.taskmanager.models.TodoModel
import com.example.taskmanager.ui.alarmManager.AlarmItem
import com.example.taskmanager.ui.alarmManager.AlarmScheduler
import com.example.taskmanager.ui.alarmManager.AlarmSchedulerImpl
import com.example.taskmanager.ui.task.TaskViewModel
import com.example.taskmanager.ui.task.TodoFragment
import kotlin.math.abs

class TodosFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var alarmScheduler: AlarmScheduler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(TaskViewModel::class.java)

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //set up recycler
        val todoRecycler: RecyclerView = binding.todosRecycler
        todoRecycler.layoutManager = LinearLayoutManager(requireContext())
        //innit
        alarmScheduler = AlarmSchedulerImpl(requireActivity())

        val todos = ArrayList<TodoModel>(0)
        val adapter = TodoAdapter(todos)
        todoRecycler.adapter = adapter

        viewModel.crudTodo.observe(viewLifecycleOwner){ crudTodo->
            when(crudTodo.action){

                Constants.TODO_ADD-> {
                    todos.add(crudTodo.model!!)
                    adapter.notifyItemInserted(todos.size-1)
                }
                Constants.TODO_DEL->{
                    //may
                    try {
                        todos.removeAt(crudTodo.position!!)
                        adapter.notifyItemRemoved(crudTodo.position)
                    }catch (_:java.lang.IndexOutOfBoundsException){

                    }
                }
                Constants.TODO_UPDATE->{
                    todos [crudTodo.position!!] = crudTodo.model!!
                    adapter.notifyItemChanged(crudTodo.position)
                }
            }
        }

        adapter.onCheck = { _, model ->
            //mark model complete
            viewModel.todoComplete(model.id,model.complete)
            // cancel alarm on delete
            AlarmItem(null,null, -abs(model.id.hashCode().toLong()))
                .let(alarmScheduler::cancel)
        }

        adapter.onItemLongClick = { menuId, position, todo ->
            when(menuId){
                R.id.delete->{
                    viewModel.deleteTodo(todo.id,position)
                    // cancel alarm on delete
                    AlarmItem(null,null, -abs(todo.id.hashCode()).toLong())
                        .let(alarmScheduler::cancel)
                }
                R.id.mark_complete->{
                    viewModel.todoComplete(todo.id, !todo.complete )
                }
                R.id.update->{
                    val todoFragment = TodoFragment()
                    todoFragment.arguments = bundleOf("taskId" to todo.taskId,
                        "position" to position,
                        "todoId" to todo.id,
                        "action" to Constants.TODO_UPDATE)
                    todoFragment.show(childFragmentManager,todoFragment.tag)
                }
            }
        }

        viewModel.todos.observe(viewLifecycleOwner){
            todos.addAll(it)
            adapter.notifyItemInserted(todos.size)
        }
        return root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}