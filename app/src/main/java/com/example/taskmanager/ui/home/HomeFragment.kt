package com.example.taskmanager.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.Constants
import com.example.taskmanager.R
import com.example.taskmanager.adapter.HomeAdapter
import com.example.taskmanager.databinding.FragmentHomeBinding
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.ui.alarmManager.AlarmItem
import com.example.taskmanager.ui.alarmManager.AlarmScheduler
import com.example.taskmanager.ui.alarmManager.AlarmSchedulerImpl
import com.example.taskmanager.ui.task.FragmentCreateTask
import com.example.taskmanager.ui.task.TaskDetailsActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var alarmScheduler: AlarmScheduler


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //
        alarmScheduler = AlarmSchedulerImpl(requireActivity())
        //

        val recyclerView = binding.taskRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        homeViewModel.tasks.observe(viewLifecycleOwner){
            val adapter = HomeAdapter(it as ArrayList<TaskModel>)
            recyclerView.adapter = adapter
            //on clicked items
            adapter.onclick = { taskId->
                val taskIntent = Intent(requireContext(),TaskDetailsActivity::class.java)
                taskIntent.putExtra("taskId",taskId)
                startActivity(taskIntent)// launch activity passing taskId
            }
            adapter.onItemLongClick = { menuId,_,model->
                when(menuId){
                    R.id.delete->{
                        homeViewModel.deleteTask(model.id!!)
                        // cancel alarm on delete
                        AlarmItem(null,null, model.id)
                            .let(alarmScheduler::cancel)
                    }
                    R.id.mark_complete-> {
                        model.status = when(model.status) {
                            Constants.TASK_STATUS_COMPLETE -> Constants.TASK_STATUS_IN_PROGRESS
                            else-> Constants.TASK_STATUS_COMPLETE
                        }
                        homeViewModel.markComplete(model)
                    }
                    R.id.update->{
                        val fragmentCreateTask = FragmentCreateTask()
                        fragmentCreateTask.arguments = bundleOf("taskId" to model.id)
                        fragmentCreateTask.show(childFragmentManager,fragmentCreateTask.tag)
                    }
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}