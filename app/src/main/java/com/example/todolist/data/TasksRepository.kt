package com.example.todolist.data

import com.example.todolist.domain.usecases.ITasksRepository

class TasksRepository(val api: Api) : ITasksRepository {
}