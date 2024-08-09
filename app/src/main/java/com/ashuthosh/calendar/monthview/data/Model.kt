package com.ashuthosh.calendar.monthview.data

import com.google.gson.annotations.SerializedName

data class TaskDetail(
    @SerializedName("title") val name: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("date") val date: Long = System.currentTimeMillis()
)

data class Task(
    @SerializedName("task_id") val id: Int,
    @SerializedName("task_detail") val detail: TaskDetail
)

data class NewTask(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("task") val detail: TaskDetail
)

data class DeleteTask(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("task_id") val taskId: Int
)

data class GetTask(
    @SerializedName("user_id") val userId: Int
)

data class Tasks(
    val tasks: List<Task>
)