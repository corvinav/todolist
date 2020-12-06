package com.example.todolist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todolist.domain.usecases.TasksListUseCase
import com.example.todolist.presentation.taskslist.TasksListViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.util.concurrent.Callable

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class TasksListViewModelTest {

    lateinit var viewModel: TasksListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        var tasksListUseCase = Mockito.mock(TasksListUseCase::class.java)
        Mockito.`when`(tasksListUseCase.getTasks(Mockito.anyLong()))
            .thenReturn(Observable.just(emptyList()))
        viewModel = TasksListViewModel(tasksListUseCase)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(object :
            Function<Callable<Scheduler?>?, Scheduler?> {
            override fun apply(t: Callable<Scheduler?>): Scheduler? {
                return Schedulers.trampoline()
            }
        })
    }

    @Test
    fun addTaskTest() {
        viewModel.addTask("TEST TASK")

        assertTrue(viewModel.tasks.value != null)
        val list = viewModel.tasks.value!!
        assertTrue(list.size == 1)
        assertEquals(list.get(0).title, "TEST TASK")
    }

    @Test
    fun removeTaskTest() {
        viewModel.addTask("TEST TASK")

        assertTrue(viewModel.tasks.value != null)
        val list = viewModel.tasks.value!!
        assertTrue(list.size == 1)
        viewModel.removeTask(list.get(0))
        assertTrue(viewModel.tasks.value != null)
        assertTrue(viewModel.tasks.value!!.isEmpty())
    }

    @Test
    fun loadDataTest() {
        viewModel.loadList()
        assertTrue(viewModel.tasks.value != null)
        assertTrue(viewModel.tasks.value!!.isEmpty())
    }
}