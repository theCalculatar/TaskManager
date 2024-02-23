package com.example.taskmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.models.TodoModel

class TodoAdapter(private val todos:ArrayList<TodoModel>): RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    var onCheck: ((position: Int, model: TodoModel) -> Unit)? = null
    var onItemLongClick: ((menuId:Int, position:Int, todo:TodoModel) -> Unit)? = null

    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_todo,parent,false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoAdapter.ViewHolder, position: Int) {
        val todo = todos[position]

        todo.title.also {
            holder.title.text = it
        }

        holder.checkBox.isChecked = todo.complete

        // no need to show due date if to-do has been done, lol
        if (todo.complete || todo.dueDate==null){
            holder.dueDate.isVisible = false
        }else{
            holder.dueDate.isVisible = true
            todo.dueDate.also {
                holder.dueDate.text = it
            }
        }

        todo.description?.also {
            holder.description.text = it
            holder.description.isVisible = true
        }?: (run{holder.description.isVisible = false})

        holder.checkBox.setOnCheckedChangeListener { _, _ ->
            todo.complete = !todo.complete
            onCheck?.invoke(position,todos[position])
        }
    }
    override fun getItemCount() =  todos.size

    //View holder for cards
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title:TextView = itemView.findViewById(R.id.title)
        val description:TextView = itemView.findViewById(R.id.description)
        val dueDate:TextView = itemView.findViewById(R.id.due_date)
        val checkBox:CheckBox = itemView.findViewById(R.id.checkbox)
        init {
            //initialize items with context menu
            itemView.setOnCreateContextMenuListener { menu, _, _ ->
                MenuInflater(context).inflate(R.menu.long_click_menu,menu)
                menu.forEach {
                    it.setOnMenuItemClickListener {menuItem->
                        when(menuItem.itemId){
                            //toggle check box
                            R.id.mark_complete->checkBox.isChecked=!checkBox.isChecked
                        }
                        onItemLongClick?.invoke(menuItem.itemId, adapterPosition, todos[adapterPosition])
                        false
                    }
                }
            }
        }
    }

}