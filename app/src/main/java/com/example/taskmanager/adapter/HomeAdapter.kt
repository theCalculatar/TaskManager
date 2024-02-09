package com.example.taskmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.Constants
import com.example.taskmanager.R
import com.example.taskmanager.models.TaskModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class HomeAdapter(private val tasks:ArrayList<TaskModel>): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_task_layout,parent,false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        val task = tasks[position]
        val days = taskDetails(task.endDate,task.startDate)

        task.title.also {
            holder.title.text = it
        }
        task.description.also {
            holder.description.text = it
        }

        task.endDate.also {
            holder.days.text = it
        }
        //days can be null if the project deadline has not been set
        if (days!=null) {
            val remainingDays = days.remainingDays
            val overallDays = days.overallDays
            val onGoingDays:Long = days.onGoingDays

            "${onGoingDays}/${overallDays}".also {
                holder.days.text = it
            }
            holder.priorityLayout.setBackgroundColor(getColor(context, R.color.yellow_500))

            holder.daysLeft.text = if (task.status==Constants.TASK_STATUS_COMPLETE){
                holder.priorityLayout.setBackgroundColor(getColor(context, R.color.green_500))
                "Completed!"
            }
            else if (days.isOverDue){
                holder.priorityLayout.setBackgroundColor(getColor(context, R.color.red_500))
                "Overdue!"
            }else if (remainingDays<1) {
                "Final day!"
            }else if (remainingDays==1L) {
                "$remainingDays Day left"
            }
            else {
                "$remainingDays Days left"
            }

            // no need to throw exception because values from database are absolute
            holder.progress.also {
                it.progress = onGoingDays.toInt()
                it.max = overallDays.toInt()
            }
        }
    }
    override fun getItemCount() =  tasks.size

    //View holder for cards
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title:TextView = itemView.findViewById(R.id.title)
        val description:TextView = itemView.findViewById(R.id.description)
        val priorityLayout:View = itemView.findViewById(R.id.priority_layout)
        val days:TextView = itemView.findViewById(R.id.number_days)
        val daysLeft:TextView = itemView.findViewById(R.id.days_remaining)
        val progress:ProgressBar = itemView.findViewById(R.id.progressBar)

    }

    private fun taskDetails(endDate_:String?, startDate_:String?): TaskDetails? {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val now = Date()
        // function parameter can b null if they where never saved, e.i. task deadline has not been set
        return if (endDate_ != null && startDate_ != null) {
            try {
                val currentDate = formatter.parse(formatter.format(now))
                val endDate = formatter.parse(endDate_)
                val startDate = formatter.parse(startDate_)

                val differenceMills = abs(startDate!!.time - endDate!!.time)
                val overallDays = TimeUnit.DAYS.convert(differenceMills, TimeUnit.MILLISECONDS)

                val differenceOverallMills = abs(currentDate!!.time - endDate.time)
                val remainingDays = TimeUnit.DAYS.convert(differenceOverallMills, TimeUnit.MILLISECONDS)

                val overdue: Boolean // if project is overdue

                //assigns on-going days if there are remaining days else assigns overall days
                val onGoingDays = if (currentDate <= endDate) {
                    overdue = false
                    overallDays - remainingDays
                } else {
                    overdue = true
                    overallDays
                }
                TaskDetails(remainingDays, overallDays, onGoingDays, overdue)
            } catch (e: ParseException) {
                e.printStackTrace()
                null
            }
        }else{
            null
        }
    }


    class TaskDetails(val remainingDays: Long, val overallDays: Long,
                      val onGoingDays:Long, val isOverDue:Boolean)

}