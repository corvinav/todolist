package com.example.todolist.presentation.taskslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.TasksListFragmentBinding

class TasksListFragment : Fragment() {

    companion object {
        fun newInstance() = TasksListFragment()
    }

    lateinit private var binding: TasksListFragmentBinding
    lateinit private var tasksAdapter: TasksAdapter
    private val viewModel: MainViewModel by viewModels()

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

        if (savedInstanceState == null) {
            viewModel.loadList()
        }
    }


    private fun initTasksList() {
        val tasksRV = binding.tasksRV
        tasksAdapter = TasksAdapter()
        tasksRV.layoutManager = LinearLayoutManager(this.context)
        tasksRV.adapter = tasksAdapter

        viewModel.tasks.observe(viewLifecycleOwner) {
            tasksAdapter.submitList(it)
        }
    }
}