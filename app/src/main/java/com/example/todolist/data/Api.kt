package com.example.todolist.data

import com.example.todolist.domain.entities.Task
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("todos")
    fun getTasks(@Query("userId") query: Long): Observable<List<Task>>


    companion object Factory {
        val SERVER = "https://jsonplaceholder.typicode.com/"
        fun create(): Api {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER)
                .build()

            return retrofit.create(Api::class.java);
        }
    }
}