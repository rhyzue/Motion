package com.rhyzue.motion.ui.schedule.tasks

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task

class TaskListAdapter internal constructor(
    context: Context,
    fragment: TasksFragment
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tasks = emptyList<Task>()
    private val parent = fragment

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskItemView: TextView = itemView.findViewById(R.id.task_text)
        val viewTaskBtn: ImageButton = itemView.findViewById(R.id.edit_task_btn)
        val removeTaskBtn: ImageButton = itemView.findViewById(R.id.remove_task_btn)
        val checkBox: CheckBox = itemView.findViewById(R.id.complete_checkbox)
        val completeIndicator: View  = itemView.findViewById(R.id.complete_indicator)
        val starBtn: ImageButton = itemView.findViewById(R.id.star_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = inflater.inflate(R.layout.task_recycler_item, parent, false)
        return TaskViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        println("BIND")
        val current = tasks[position]
        println(current)
        holder.taskItemView.text = current.name
        holder.viewTaskBtn.setOnClickListener { parent.onEditTask(current.id) }
        holder.removeTaskBtn.setOnClickListener { parent.onRemoveTask(current.id) }
        holder.starBtn.setOnClickListener{ parent.onStarTask(current.id, holder.starBtn)}
        holder.checkBox.setOnClickListener { parent.onCompleteTask(current.id)}
        holder.checkBox.buttonTintList =parent.getTypeColor(current.id)
        holder.checkBox.isChecked  = current.complete

        if (current.complete)
            holder.completeIndicator.visibility = View.VISIBLE
        else
            holder.completeIndicator.visibility = View.GONE
    }

    internal fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        println("ADAPTER")
        for(i in tasks){
            println(i)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = tasks.size
}