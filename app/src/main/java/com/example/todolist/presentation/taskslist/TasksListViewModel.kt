package com.example.todolist.presentation.taskslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.domain.entities.Task
import com.example.todolist.domain.usecases.TasksListUseCase
import com.example.todolist.utils.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

class TasksListViewModel(private val tasksListUseCase: TasksListUseCase) : ViewModel(),
    TasksAdapter.OnTaskAction {
    private val USER_ID = 1L

    private var tasksLoadingDisposable: Disposable? = null

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
        safelyDispose(tasksLoadingDisposable)
        _dataLoading.value = true
        tasksLoadingDisposable = tasksListUseCase.getTasks(USER_ID)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getTasksObserver())
    }

    private fun getTasksObserver(): DisposableObserver<List<Task>> =
        object : DisposableObserver<List<Task>>() {
            override fun onNext(tasksList: List<Task>) {
                _tasks.value = tasksList
            }

            override fun onError(e: Throwable) {
                _showError.value = Event(Any())
                _dataLoading.value = false
                e.printStackTrace()
            }

            override fun onComplete() {
                _dataLoading.value = false
            }
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

    override fun onCleared() {
        super.onCleared()
        safelyDispose(tasksLoadingDisposable)
    }

    private fun safelyDispose(vararg disposables: Disposable?) {
        for (subscription in disposables) {
            if (subscription != null && !subscription.isDisposed) {
                subscription.dispose()
            }
        }
    }

    class ViewModelFactory(val tasksListUseCase: TasksListUseCase) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TasksListViewModel(tasksListUseCase) as T
        }
    }
}

//logic of getting new Id isn't defined so I get max value+1.This function shouldn't be here
fun getNextId(tasks: List<Task>): Long =
    if (tasks.isEmpty()) {
        0
    } else {
        tasks.maxOf { it.id } + 1
    }