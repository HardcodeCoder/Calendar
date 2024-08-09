package com.ashuthosh.calendar.monthview.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId

class TaskRepo(
    private val source: TaskSource,
    private val userId: Int
) {

    private val _tasksFlow = MutableStateFlow(listOf<Task>())
    val tasksFlow: StateFlow<List<Task>> = _tasksFlow
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    fun setDate(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

    suspend fun fetchAllTasks() = withContext(Dispatchers.Default) {
        val tasks = source.getAllTasks(GetTask(userId)).tasks.sortedBy { it.detail.date }
        _tasksFlow.emit(tasks)
    }

    suspend fun fetchTasksOnlyForDate() = withContext(Dispatchers.Default) {
        val startOfDay = LocalDateTime.of(year, month, day, 0, 0, 0)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        val endOfDay = startOfDay + MILLISECONDS_IN_A_DAY - 1

        val filteredValue = source.getAllTasks(GetTask(userId)).tasks.filter {
            it.detail.date in startOfDay..endOfDay
        }
        _tasksFlow.emit(filteredValue.toMutableList())
    }

    suspend fun addTask(name: String, desc: String) = withContext(Dispatchers.Default) {
        val selectDayEpochMills = LocalDateTime.of(year, month, day, 0, 0, 0)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        source.addTask(NewTask(userId, TaskDetail(name, desc, selectDayEpochMills)))
        fetchTasksOnlyForDate()
    }

    suspend fun deleteTask(taskId: Int) = withContext(Dispatchers.Default) {
        source.deleteTask(DeleteTask(userId, taskId))
        fetchTasksOnlyForDate()
    }

    companion object {
        private const val MILLISECONDS_IN_A_DAY = 86400000
    }
}