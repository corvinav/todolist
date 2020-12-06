package com.example.todolist.presentation.taskslist

import android.graphics.Paint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.TaskItemBinding
import com.example.todolist.domain.entities.Task

class TasksAdapter(private val onTaskAction: OnTaskAction) :
    ListAdapter<Task, TasksAdapter.ViewHolder>(TaskDiffCallback()) {
    private val expanded = mutableMapOf<Long, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, expanded[item.id] ?: false, onTaskAction) {
            expanded[item.id] = it
        }
    }

    fun remove(position: Int) {
        val task = getItem(position)
        onTaskAction.removeTask(task)
        expanded.remove(task.id)
    }

    class ViewHolder private constructor(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var isExpanded = false

        fun bind(
            item: Task,
            _isExpanded: Boolean,
            onTaskAction: OnTaskAction,
            onTitleClick: (Boolean) -> Unit
        ) {
            isExpanded = _isExpanded
            binding.task = item
            binding.executePendingBindings()
            val completedCB = binding.isCompletedCB

            completedCB.setOnClickListener {
                val isCompleted = completedCB.isChecked
                onTaskAction.completeTask(item, isCompleted)
                updateCheckedState(isCompleted)
            }

            val titleTV = binding.titleTV
            titleTV.setOnClickListener {
                isExpanded = !isExpanded
                onTitleClick(isExpanded)
                updateExpandedState()
            }

            updateExpandedState()
            updateCheckedState(item.completed)
        }

        private fun updateExpandedState() {
            val titleTV = binding.titleTV
            if (isExpanded) {
                titleTV.isSingleLine = false
                titleTV.ellipsize = null
            } else {
                titleTV.isSingleLine = true
                titleTV.ellipsize = TextUtils.TruncateAt.END
            }

        }

        private fun updateCheckedState(isCompleted: Boolean) {
            val root = binding.root
            if (isCompleted) {
                setChecked(root)
            } else {
                setUnchecked(root)
            }
        }

        private fun setChecked(root: View) {
            root.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    R.color.done_task_back
                )
            )
            binding.titleTV.apply {
                setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.grey_text
                    )
                )
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

        private fun setUnchecked(root: View) {
            root.setBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    R.color.white
                )
            )
            binding.titleTV.apply {
                setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        R.color.black
                    )
                )
                paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    interface OnTaskAction {
        fun completeTask(task: Task, isCompleted: Boolean)
        fun removeTask(Task: Task)
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}