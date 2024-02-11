package com.example.taskmanager.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TodoFragment:BottomSheetDialogFragment() {
    companion object{
        const val TAG = "TodoFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_todo,container)
        //
        val title = view.findViewById<EditText>(R.id.title)
        val description = view.findViewById<EditText>(R.id.description)
        val dueDate = view.findViewById<TextView>(R.id.save)
        val alert = view.findViewById<ImageView>(R.id.alert)
        val taskSpinner = view.findViewById<Spinner>(R.id.task_spinner)
        val taskPicker = view.findViewById<FrameLayout>(R.id.task_picker)
        //
        var taskId:Long?

        arguments?.apply {
            taskId = this.getLong("taskId",-1L)
            taskPicker.isVisible = taskId==-1L
        }

        viewModel.getTodo(2L).observe(viewLifecycleOwner){
            val task = ArrayList<String>().apply {
                this.add("Select task")
                it.forEach{ todo-> this.add(todo.title) }
            }

            val priorityCallback = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    taskId = if (position==1){
                        null
                    }else (position-1).toLong()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,task)
                .also { arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    taskSpinner.adapter = arrayAdapter
                    taskSpinner.onItemSelectedListener = priorityCallback
                }
        }


//        viewModel.addTodo()

        return view
    }
    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}