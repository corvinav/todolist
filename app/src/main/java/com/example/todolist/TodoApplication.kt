package com.example.todolist

import android.app.Application
import com.example.todolist.data.Api

class TodoApplication: Application() {

    val api: Api by lazy {
        Api.create()
    }

}