package com.example.taskmanager.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.Constants
import com.example.taskmanager.R
import com.example.taskmanager.models.TaskModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentCreateTask: BottomSheetDialogFragment() {

    companion object{
        const val TAG = "NewTaskTag"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_create_task,container)
        //fragment views
        val title = view.findViewById<EditText>(R.id.title)
        val description = view.findViewById<EditText>(R.id.description)
        val createTask = view.findViewById<TextView>(R.id.create_task)
        val prioritySpinner = view.findViewById<Spinner>(R.id.task_spinner)

        var priority = ""

        val priorityCallback = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = parent?.getItemAtPosition(position) as String
                priority = item.split(" ")[0]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                val item = parent?.getItemAtPosition(1) as String
                priority = item.split(" ")[0]

            }
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
            //add to local database
            viewModel.addTask(TaskModel(null,title.text.toString(),
                Constants.TASK_STATUS_STARTED,descriptionTxt,null,null,priority))
            dismiss()
        }
        // spinner adapter
        ArrayAdapter.createFromResource(requireContext(),
            R.array.priority,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            prioritySpinner.adapter = arrayAdapter
            prioritySpinner.onItemSelectedListener = priorityCallback
        }
        return view
    }
    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

}