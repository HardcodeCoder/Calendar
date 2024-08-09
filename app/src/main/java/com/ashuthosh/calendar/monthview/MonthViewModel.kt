package com.ashuthosh.calendar.monthview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashuthosh.calendar.monthview.data.Task
import com.ashuthosh.calendar.monthview.data.TaskRepo
import com.ashuthosh.calendar.monthview.data.TaskService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MonthViewModel : ViewModel() {

    private val userId = 5000
    private val repo: TaskRepo

    init {
        val taskSource = Retrofit.Builder()
            .baseUrl("http://dev.frndapp.in:8085")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskService::class.java)
        repo = TaskRepo(taskSource, userId)
    }

    fun setSelectedDay(year: Int, month: Int, day: Int) {
        repo.setDate(year, month, day)
        viewModelScope.launch { repo.fetchTasksOnlyForDate() }
    }

    fun getTasks() = repo.tasksFlow

    fun addTask(name: String, desc: String) = viewModelScope.launch { repo.addTask(name, desc) }

    fun deleteTask(task: Task) = viewModelScope.launch { repo.deleteTask(task.id) }
}