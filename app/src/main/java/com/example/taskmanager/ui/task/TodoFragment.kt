package com.example.taskmanager.ui.task

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.Constants
import com.example.taskmanager.R
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel
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
        val save = view.findViewById<TextView>(R.id.save)
        val alert = view.findViewById<ImageView>(R.id.alert)
        val taskSpinner = view.findViewById<Spinner>(R.id.task_spinner)
        val taskPicker = view.findViewById<FrameLayout>(R.id.task_picker)
        //
        var taskId:Long?=null

        arguments?.apply {
            taskId = this.getLong("taskId",-1L)
            taskPicker.isVisible = taskId==-1L
        }

        viewModel.allTask.observe(viewLifecycleOwner){
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

        save.setOnClickListener {
            if (title.text.isEmpty()){
                title.error = getString(R.string.non_empty_field)
                Toast.makeText(requireContext(),getString(R.string.fill_all_required_fields), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            //add to local database
            viewModel.addTodo(
                TodoModel(null,taskId,title.text.toString(),
                description.text.toString(),false,null)
            )
            dismiss()
        }

        return view
    }
    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
}

private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}