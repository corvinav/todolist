package com.example.todolist.domain.usecases

import com.example.todolist.domain.entities.Task
import io.reactivex.Observable

interface ITasksRepository {
    fun getTasks(userId: Long): Observable<List<Task>>
}