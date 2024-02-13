package com.example.taskmanager.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.adapter.HomeAdapter
import com.example.taskmanager.adapter.TodoAdapter
import com.example.taskmanager.adapter.TodoFragmentAdapter
import com.example.taskmanager.databinding.FragmentDashboardBinding
import com.example.taskmanager.models.TodoModel
import com.example.taskmanager.ui.task.TodoFragment

class TodosFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val todosViewModel =
            ViewModelProvider(this).get(TodosViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val todoRecycler: RecyclerView = binding.todosRecycler
        todoRecycler.layoutManager = LinearLayoutManager(requireContext())

        binding.newTaskFab.setOnClickListener {
            val todoFragment = TodoFragment()
            todoFragment.show(childFragmentManager, TodoFragment.TAG)
        }

        todosViewModel.todos.observe(viewLifecycleOwner){
            val adapter = TodoAdapter(it as ArrayList<TodoModel>)
            todoRecycler.adapter = adapter
            adapter.onCheck = {adapterPosition,complete->
                todosViewModel.todoComplete(adapterPosition,complete)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}