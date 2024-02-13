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
import com.example.taskmanager.ui.task.TaskViewModel
import java.util.Calendar

class TimePickerFragment : DialogFragment(),
    OnTimeSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)


        // Use the current date as the default date in the picker.
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of DatePickerDialog and return it.
        return TimePickerDialog(requireContext(), this, hour, minute, true)
    }

    /**
     * Grabs the date and passes it to processDatePickerResult().
     *
     * @param datePicker  The date picker view
     * @param year  The year chosen
     * @param month The month chosen
     * @param day   The day chosen
     */


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
//        Log.d(tag, LocalDate.of(year, month + 1, day).month.toString())
//        Log.d(tag, LocalDate.of(year, month, day).toString())    }
        }
}