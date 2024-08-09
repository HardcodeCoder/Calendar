package com.ashuthosh.calendar.monthview.data

interface TaskSource {

    suspend fun getAllTasks(getTask: GetTask): Tasks

    suspend fun addTask(newTask: NewTask)

    suspend fun deleteTask(deleteTask: DeleteTask)
}