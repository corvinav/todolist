package com.example.todolist.presentation.taskslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.domain.entities.Task
import com.example.todolist.utils.Event

class TasksListViewModel : ViewModel(),
    TasksAdapter.OnTaskAction {
    private val USER_ID = 1L

    private val _tasks = MutableLiveData<List<Task>>().apply { value = emptyList() }
    val tasks: LiveData<List<Task>> = _tasks

    private val _dataLoading = MutableLiveData<Boolean>().apply { value = false }
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showError = MutableLiveData<Event<Any>>()
    val showError: LiveData<Event<Any>> = _showError

    override fun completeTask(task: Task, isCompleted: Boolean) {
        task.completed = isCompleted
        //todo some functionality to update data on server or DB
    }

    override fun removeTask(task: Task) {
        _tasks.value?.let {
            val taskList = it.toMutableList()
            taskList.remove(task)
            _tasks.value = taskList
        }
    }

    fun loadList() {
        val list = mutableListOf<Task>()
        list.add(Task(1, 1, "aergsearg", true))
        list.add(Task(1, 2, "aergseargvdfv", false))
        list.add(Task(1, 3, "11111111aergsearg", false))
        list.add(Task(1, 4, "2222222aergsearg", true))
        list.add(Task(1, 5, "3333333333aergsea lm;lm ;lkm;lm;l  ',';l,';,.';,lkmn;l,' rg", false))
        _tasks.value = list
    }

    fun addTask(title: String): Boolean {
        val oldList = _tasks.value
        if (oldList != null && title.trim().isNotEmpty()) {
            val newList: MutableList<Task> = oldList.toMutableList()
            val task = Task(USER_ID, getNextId(oldList), title, false)
            newList.add(0, task)
            _tasks.value = newList
            return true
        }
        return false
    }
}

//logic of getting new Id isn't defined so I get max value+1.This function shouldn't be here
fun getNextId(tasks: List<Task>): Long =
    if (tasks.isEmpty()) {
        0
    } else {
        tasks.maxOf { it.id } + 1
    }