package com.example.taskmanager.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.adapter.HomeAdapter
import com.example.taskmanager.databinding.FragmentHomeBinding
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.ui.task.FragmentCreateTask
import com.example.taskmanager.ui.task.TaskDetailsActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val newTaskFab = binding.newTaskFab
        val recyclerView = binding.taskRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        newTaskFab.setOnClickListener {
            FragmentCreateTask().show(childFragmentManager,FragmentCreateTask.TAG)
        }

        homeViewModel.tasks.observe(viewLifecycleOwner){
            val adapter = HomeAdapter(it as ArrayList<TaskModel>)
            recyclerView.adapter = adapter
            adapter.onclick = { taskId->
                val taskIntent = Intent(requireContext(),TaskDetailsActivity::class.java)
                taskIntent.putExtra("taskId",taskId)
                startActivity(taskIntent)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}