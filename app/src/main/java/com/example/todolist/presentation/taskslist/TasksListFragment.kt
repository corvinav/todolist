package com.example.todolist.presentation.taskslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.TodoApplication
import com.example.todolist.data.TasksRepository
import com.example.todolist.databinding.TasksListFragmentBinding
import com.example.todolist.domain.usecases.TasksListUseCase

class TasksListFragment : Fragment() {

    companion object {
        fun newInstance() = TasksListFragment()
    }

    private lateinit var binding: TasksListFragmentBinding
    private lateinit var tasksAdapter: TasksAdapter
    var isTaskAdded = false

    private val viewModel: TasksListViewModel by viewModels() {
        val api = (activity?.application as TodoApplication).api
        if (api != null) {
            TasksListViewModel.ViewModelFactory(
                TasksListUseCase(
                    TasksRepository(api)
                )
            )
        } else {
            throw RuntimeException("No activity!")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TasksListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTasksList()
        initAddButton()
        initLoadingUI()

        if (savedInstanceState == null) {
            viewModel.loadList()
        }
    }

    private fun initLoadingUI() {
        viewModel.dataLoading.observe(viewLifecycleOwner) {
            val progressBar = binding.progressBar
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
        viewModel.showError.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context, "Data loading error!", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun initTasksList() {
        val tasksRV = binding.tasksRV
        tasksAdapter = TasksAdapter(viewModel)
        tasksRV.layoutManager = LinearLayoutManager(this.context)
        tasksRV.adapter = tasksAdapter

        ItemTouchHelper(SwipeToDeleteCallback(tasksAdapter))
            .attachToRecyclerView(tasksRV)

        viewModel.tasks.observe(viewLifecycleOwner) {
            tasksAdapter.submitList(it) {
                if (isTaskAdded) {
                    tasksRV.smoothScrollToPosition(0)
                    isTaskAdded = false
                }
            }
        }
    }

    private fun initAddButton() {
        binding.addBtn.setOnClickListener() {
            isTaskAdded = viewModel.addTask(binding.taskNameET.text.toString())
            if (isTaskAdded) {
                binding.taskNameET.text.clear()
            }
        }
    }
}