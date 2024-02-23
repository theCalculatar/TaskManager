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

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.taskmanager.R
import java.util.*

class DatePickerFragment : DialogFragment(),
    OnDateSetListener {

    private var itemId:Long?= null
    private var title:String?= null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let {
            itemId = it.getLong("itemId")
            title = it.getString("title",null)
        }

        // Use the current date as the default date in the picker.
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        // Create a new instance of DatePickerDialog and return it.
        val datePicker = DatePickerDialog(requireActivity(), this, year, month, day)
        datePicker.datePicker.minDate = c.time.time
        return datePicker
    }

    /**
     * Grabs the date and passes it to processDatePickerResult().
     *
     * @param datePicker  The date picker view
     * @param year  The year chosen
     * @param month The month chosen
     * @param day   The day chosen
     */
    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        // Set the date for Task.
            requireActivity().supportFragmentManager.let {
                val timePickerFragment = TimePickerFragment()
                //pass date to time fragment to complete time set
                timePickerFragment.arguments = bundleOf("itemId" to itemId, "title" to title,
                    "year" to year,
                    "month" to month+1, "day" to day, )
                timePickerFragment.show(it, "timePicker") }

    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersBottomDialog
    }


}