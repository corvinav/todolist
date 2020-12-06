package com.example.todolist.domain.usecases

import com.example.todolist.domain.entities.Task
import io.reactivex.Observable

class TasksListUseCase(private val taskRepository: ITasksRepository) {
    fun getTasks(userId: Long): Observable<List<Task>> {
        return taskRepository.getTasks(userId)
    }
}