package com.ashuthosh.calendar

import com.ashuthosh.calendar.monthview.data.Task
import com.ashuthosh.calendar.monthview.data.TaskDetail
import com.ashuthosh.calendar.monthview.data.TaskRepo
import com.ashuthosh.calendar.monthview.data.TaskSource
import com.ashuthosh.calendar.monthview.data.Tasks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskRepoTest {

    private val source = mockk<TaskSource>()
    private val repo = TaskRepo(source, 100)

    @BeforeAll
    fun setUp() {
        val now = LocalDateTime.now()
        repo.setDate(now.year, now.monthValue, now.dayOfMonth)
    }

    @Test
    fun `Get empty tasks for date`() = runTest {
        coEvery { source.getAllTasks(any()) } returns Tasks(listOf())

        val flowResult = mutableListOf<List<Task>>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repo.tasksFlow.toList(flowResult)
        }

        repo.fetchTasksOnlyForDate()
        assertEquals(1, flowResult.size)
        assertEquals(0, flowResult[0].size)
    }

    @Test
    fun `Get one task for date`() = runTest {
        val task = Task(200, TaskDetail("", ""))
        coEvery { source.getAllTasks(any()) } returns Tasks(listOf(task))

        val flowResult = mutableListOf<List<Task>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repo.tasksFlow.toList(flowResult)
        }

        repo.fetchTasksOnlyForDate()

        assertEquals(2, flowResult.size)
        assertEquals(1, flowResult[1].size)
        assertEquals(200, flowResult[1][0].id)
    }
}