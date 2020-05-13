package com.rhyzue.motion.ui.schedule.month

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.rhyzue.motion.R
import com.rhyzue.motion.data.Task

class TaskAdapterMini internal constructor(
    context: Context,
    fragment: MonthFragment
) : RecyclerView.Adapter<TaskAdapterMini.TaskViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tasks = emptyList<Task>()
    private val parent = fragment

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskItemView: TextView = itemView.findViewById(R.id.task_text_mini)
        val checkBox: CheckBox = itemView.findViewById(R.id.complete_checkbox_mini)
        val completeIndicator: View  = itemView.findViewById(R.id.complete_indicator_mini)
        val starBtn: ImageView = itemView.findViewById(R.id.star_btn_mini)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = inflater.inflate(R.layout.task_recycler_item_mini, parent, false)
        return TaskViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = tasks[position]
        holder.taskItemView.text = current.name
        holder.checkBox.buttonTintList =parent.getTypeColor(current.id)
        holder.checkBox.isChecked  = current.complete

        if(current.starred)
            holder.starBtn.setImageResource(R.drawable.star_filled)
        else
            holder.starBtn.setImageResource(R.drawable.star_unfilled)

        if (current.complete)
            holder.completeIndicator.visibility = View.VISIBLE
        else
            holder.completeIndicator.visibility = View.GONE
    }

    internal fun setTasks(tasks: List<Task>) {

        val sorted = tasks.sortedWith(compareBy<Task> { it.complete }.thenByDescending { it.starred })

        this.tasks=sorted
        notifyDataSetChanged()
    }

    override fun getItemCount() = tasks.size
}