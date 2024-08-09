package com.ashuthosh.calendar.monthview.data

class LocalDataSource : TaskSource {

    private val now = System.currentTimeMillis()
    private val tasks = mutableListOf(
        Task(1, TaskDetail("First run", "My first Task", 0)),
        Task(2, TaskDetail("Task 2", "Something...", now)),
        Task(3, TaskDetail("Task 3", "Something...Something...", now)),
        Task(4, TaskDetail("Task 4", "Something...", 0)),
        Task(5, TaskDetail("Task 5", "Something...Something...Something...", now)),
        Task(6, TaskDetail("Task 6", "Something...", now)),
        Task(7, TaskDetail("Task 7", "", 0)),
        Task(8, TaskDetail("Task 8", "Something...", 0)),
        Task(9, TaskDetail("Task 9", "Something...", now)),
        Task(10, TaskDetail("Task 10", "", now)),
        Task(11, TaskDetail("Task 11", "", now)),
    )
    private var inc = 12

    override suspend fun getAllTasks(getTask: GetTask): Tasks = Tasks(tasks)

    override suspend fun addTask(newTask: NewTask) {
        tasks.add(Task(inc++, newTask.detail))
    }

    override suspend fun deleteTask(deleteTask: DeleteTask) {
        tasks.removeIf { it.id == deleteTask.taskId }
    }
}