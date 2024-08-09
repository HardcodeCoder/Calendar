package com.ashuthosh.calendar.monthview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ashuthosh.calendar.R
import com.ashuthosh.calendar.monthview.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskAdapter(
    context: Context,
    private val onDelete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var tasks = mutableListOf<Task>()

    suspend fun setData(newTasks: List<Task>) = withContext(Dispatchers.Default) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize(): Int = tasks.size

            override fun getNewListSize(): Int = newTasks.size

            override fun areItemsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean = tasks[oldItemPosition] == newTasks[newItemPosition]

            override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean = tasks[oldItemPosition] == newTasks[newItemPosition]
        })

        withContext(Dispatchers.Main) {
            tasks.clear()
            tasks.addAll(newTasks)
            diff.dispatchUpdatesTo(this@TaskAdapter)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(inflater.inflate(R.layout.task_item, parent, false))
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) = holder.bind(tasks[position], onDelete)

    override fun getItemCount(): Int = tasks.size

    class TaskViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.task_name)
        private val description: TextView = itemView.findViewById(R.id.task_desc)
        private val delete: ImageView = itemView.findViewById(R.id.task_delete_btn)

        fun bind(task: Task, onDelete: (Task) -> Unit) {
            name.text = task.detail.name
            description.text = task.detail.description
            delete.setOnClickListener { onDelete(task) }
        }
    }
}