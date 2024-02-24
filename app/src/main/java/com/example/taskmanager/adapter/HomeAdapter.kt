package com.example.taskmanager.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.Constants
import com.example.taskmanager.R
import com.example.taskmanager.models.TaskModel
import com.example.taskmanager.models.TodoModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class HomeAdapter(private val tasks:ArrayList<TaskModel>): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    var onclick: ((Long) -> Unit)? = null
    var onItemLongClick: ((menuId:Int, position:Int, todo: TaskModel) -> Unit)? = null

    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_task_layout,parent,false)
        context = parent.context
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        val task = tasks[position]
        val days = taskDetails(task.dueDate,task.startDate)

        task.title.also {
            holder.title.text = it
        }

        task.description?.also {
            holder.description.isVisible = true
            holder.description.text = it
        }?: (run{
            holder.description.isVisible = false
        })

        task.dueDate?.also {
            holder.days.text = it
        }

        holder.priorityLayout.setBackgroundColor(
            getColor(holder.itemView.context, getPriorityColor(priority = task.priority!!)))

        //days can be null if the project deadline has not been set
        if (days!=null) {
            //
            holder.progress.isVisible = true
            holder.daysLeft.isVisible =true
            holder.days.isVisible =true
            //
            val remainingDays = days.remainingDays
            val overallDays = days.overallDays
            val onGoingDays:Long = days.onGoingDays
            //
            "${onGoingDays}/${overallDays}".also {
                holder.days.text = it
            }

            holder.daysLeft.text = if (task.status==Constants.TASK_STATUS_COMPLETE){
                holder.days.background = getDrawable(context,R.drawable.gradient_green)
                holder.progress.progressTintList = ColorStateList.valueOf(getColor(context, R.color.green_500))
                "Completed!"
            }else if (days.isOverDue){
                holder.days.background = getDrawable(context,R.drawable.gradient_red)
                holder.progress.progressTintList = ColorStateList.valueOf(getColor(context, R.color.light_red))
                "Overdue!"
            }else if (remainingDays<1L) {
                holder.days.background = getDrawable(context,R.drawable.gradient_red)
                holder.progress.progressTintList = ColorStateList.valueOf(getColor(context, R.color.light_red))
                "Final day!"
            }else if (remainingDays==1L) {
                holder.days.background = getDrawable(context,R.drawable.gradient_blue)
                holder.progress.progressTintList = ColorStateList.valueOf(getColor(context, R.color.light_blue))
                "$remainingDays Day left"
            }
            else {
                holder.days.background = getDrawable(context,R.drawable.gradient_green)
                holder.progress.progressTintList = ColorStateList.valueOf(getColor(context, R.color.light_green))//                holder.progress.horizontalScrollbarTrackDrawable = (getDrawable(context, R.drawable.gradient_green))
                "$remainingDays Days left"
            }

            holder.progress.also {
                it.progress = onGoingDays.toInt()+1
                it.max = overallDays.toInt()+1
            }
        }

    }
    override fun getItemCount() =  tasks.size

    //View holder for cards
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val title:TextView = itemView.findViewById(R.id.title)
        val description:TextView = itemView.findViewById(R.id.description)
        private val options:ImageView = itemView.findViewById(R.id.options_task)
        val priorityLayout:View = itemView.findViewById(R.id.priority_layout)
        val days:TextView = itemView.findViewById(R.id.number_days)
        val daysLeft:TextView = itemView.findViewById(R.id.days_remaining)
        val progress:ProgressBar = itemView.findViewById(R.id.progressBar)
        init {
            itemView.setOnClickListener {
                onclick?.invoke(tasks[adapterPosition].id!!)
            }
            //initialize items with context menu
            options.setOnClickListener {
                val menu = PopupMenu(it.context,it)
                menu.inflate(R.menu.long_click_menu)
                menu.setOnMenuItemClickListener {menuItem->
                        onItemLongClick?.invoke(menuItem.itemId, adapterPosition, tasks[adapterPosition])
                        false
                    }
                menu.show()

            }
        }
    }

    private fun taskDetails(endDate_:String?, startDate_:String?): TaskDetails? {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val now = Date()
        // function parameter can be null if they where never saved, e.i. task deadline has not been set
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

    private fun getPriorityColor(priority: String):Int{
        val priorityArray = context.resources.getStringArray(R.array.priority)
        return when(priority){
            priorityArray[0]->R.color.green_500
            priorityArray[1]->R.color.yellow_500
            else->R.color.red_500
        }
    }

    class TaskDetails(val remainingDays: Long, val overallDays: Long,
                      val onGoingDays:Long, val isOverDue:Boolean)

}