package com.example.taskmanager.ui.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.room.Update
import com.example.taskmanager.Constants
import com.example.taskmanager.R
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.ui.alarmManager.AlarmItem
import com.example.taskmanager.ui.alarmManager.AlarmScheduler
import com.example.taskmanager.ui.alarmManager.AlarmSchedulerImpl
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.abs

class FragmentCreateTask: BottomSheetDialogFragment() {

    companion object{
        const val TAG = "NewTaskTag"
    }
    private lateinit var alarmScheduler: AlarmScheduler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_create_task,container)
        //
        alarmScheduler = AlarmSchedulerImpl(requireActivity())
        //fragment views
        val title = view.findViewById<EditText>(R.id.title)
        val description = view.findViewById<EditText>(R.id.description)
        val createTask = view.findViewById<TextView>(R.id.create_task)
        val action = view.findViewById<TextView>(R.id.action)
        val prioritySpinner = view.findViewById<Spinner>(R.id.task_spinner)
        val delete = view.findViewById<ImageView>(R.id.delete_task)
        //
        var priority = ""
        val priorityArray = resources.getStringArray(R.array.priority)
        var fromTask = false
        var taskId:Long? = null
        var startDate:String? = null
        var endDate:String? = null

        //
        arguments?.let{ bundle->
            taskId = bundle.getLong("taskId")
            fromTask = true
            delete.isVisible = true
            "Update Task".also {
                createTask.text = it
                action.text = it
            }

            viewModel.getTask(taskId!!).observe(viewLifecycleOwner){
                title.setText(it.title,TextView.BufferType.EDITABLE)
                description.setText(it.description,TextView.BufferType.EDITABLE)
                startDate = it.startDate
                endDate = it.dueDate
                priority = it.priority!!

                prioritySpinner.setSelection(when (priority){
                    priorityArray[0]->0
                    priorityArray[1]->1
                    else->2
                })
            }
        }
        //spinner callback
        val priorityCallback = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                priority = parent?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        // spinner adapter
        ArrayAdapter(requireActivity(),
            android.R.layout.simple_spinner_item,
            priorityArray
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            prioritySpinner.adapter = arrayAdapter
            prioritySpinner.onItemSelectedListener = priorityCallback
        }

        delete.setOnClickListener {
            //using live data to update both fragments
            viewModel.deleteTask(taskId!!)
            // cancel alarm on delete
            AlarmItem(null,null, taskId!!)
                .let(alarmScheduler::cancel)
            dismiss()
        }

        createTask.setOnClickListener {
            val descriptionTxt = if(description.text.isNullOrEmpty()) {
                null
            }else{
                description.text.toString()
            }
            if (title.text.isEmpty()){
                title.error = getString(R.string.non_empty_field)
                Toast.makeText(requireContext(),getString(R.string.fill_all_required_fields),Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val taskModel = TaskModel(
                taskId, title.text.toString(),
                Constants.TASK_STATUS_STARTED,
                descriptionTxt, startDate, endDate, priority
            )
            //add to local database
            if (fromTask){
                viewModel.updateTask(taskModel)
            }else {
                viewModel.addTask(taskModel)
            }
            dismiss()
        }
        return view
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

}