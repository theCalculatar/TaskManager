/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.taskmanager.ui.pickerdate

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.ui.alarmManager.AlarmItem
import com.example.taskmanager.ui.alarmManager.AlarmScheduler
import com.example.taskmanager.ui.alarmManager.AlarmSchedulerImpl
import com.example.taskmanager.ui.task.TaskViewModel
import java.time.LocalDateTime
import java.util.*

class TimePickerFragment : DialogFragment(),
    OnTimeSetListener {

    private var year:Int = 0
    private var month:Int = 0
    private var dayOfMonth:Int = 0
    private var itemId:Long = 0
    private var title:String? = null
    private lateinit var alarmScheduler: AlarmScheduler

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        alarmScheduler = AlarmSchedulerImpl(requireActivity())

        arguments?.let {
            year = it.getInt("year")
            month = it.getInt("month")
            dayOfMonth = it.getInt("day")
            itemId = it.getLong("itemId")
            title = it.getString("title",null)
        }
        // Use the current date as the default date in the picker.
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of DatePickerDialog and return it.
        return TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    /**
     * Grabs the date and passes it to timePickerResult().
     *
     * @param hourOfDay  The hour chosen
     * @param minute  The minutes chosen
     */
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        val viewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
        val dueDate = LocalDateTime.of(year, month, dayOfMonth,hourOfDay,minute)

        //update Database only if item is task
        if (itemId>0) viewModel.addDate(itemId,dueDate.toString())
        //get updated task or to do model
        title?.let {title->
            dueDate.let {
                val alarmItem = AlarmItem(
                    alarmTime = it.plusSeconds(5),
                    message = try {
                        "${title.substring(0..10)}..."
                    } catch (e: StringIndexOutOfBoundsException) {
                        title
                    },
                    itemId = itemId
                )
                // cancel previous time if any before setting new one
                alarmItem.let(alarmScheduler::cancel)
                alarmItem.let(alarmScheduler::schedule)
            }
        }?: viewModel.addDate(dueDate) // if no arguments were supplied add date temporarily on view model
    }
}