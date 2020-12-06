package com.example.todolist.domain.entities

data class Task(
    val userId: Long,
    val id: Long,
    var title: String,
    var completed: Boolean
) {
}