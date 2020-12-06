package com.example.todolist.data

import com.example.todolist.domain.entities.Task
import com.example.todolist.domain.usecases.ITasksRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class TasksRepository(private val api: Api) : ITasksRepository {
    override fun getTasks(userId: Long): Observable<List<Task>> {
        return api.getTasks(userId)
            .subscribeOn(Schedulers.io())

    }
}