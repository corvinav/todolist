package com.example.todolist.presentation.taskslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.domain.entities.Task

class MainViewModel : ViewModel() {
    private val _tasks = MutableLiveData<List<Task>>().apply { value = emptyList() }
    val tasks: LiveData<List<Task>> = _tasks

    fun loadList() {
        val list = mutableListOf<Task>()
        list.add(Task(1,1,"aergsearg",true))
        list.add(Task(1,2,"aergseargvdfv",false))
        list.add(Task(1,3,"11111111aergsearg",false))
        list.add(Task(1,4,"2222222aergsearg",true))
        list.add(Task(1,5,"3333333333aergsea lm;lm ;lkm;lm;l  ',';l,';,.';,lkmn;l,' rg",false))
        _tasks.value=list
    }
}