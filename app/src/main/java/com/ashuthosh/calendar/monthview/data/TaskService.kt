package com.ashuthosh.calendar.monthview.data

import retrofit2.http.Body
import retrofit2.http.POST

interface TaskService : TaskSource {

    @POST("api/getCalendarTaskList")
    override suspend fun getAllTasks(@Body getTask: GetTask): Tasks

    @POST("api/storeCalendarTask")
    override suspend fun addTask(@Body newTask: NewTask)

    @POST("api/deleteCalendarTask")
    override suspend fun deleteTask(@Body deleteTask: DeleteTask)
}