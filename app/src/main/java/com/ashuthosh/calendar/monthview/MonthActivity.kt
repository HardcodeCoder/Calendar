package com.ashuthosh.calendar.monthview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashuthosh.calendar.R
import com.ashuthosh.calendar.findDaysAndStartDay
import com.ashuthosh.calendar.getMonthText
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.DynamicColors
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class MonthActivity : AppCompatActivity() {

    private val viewModel: MonthViewModel by viewModels()
    private lateinit var tasksView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        DynamicColors.applyToActivityIfAvailable(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_month)

        val year = intent.getIntExtra(KEY_YEAR, 2024)
        val month = intent.getIntExtra(KEY_MONTH, 8)

        tasksView = findViewById(R.id.task_list)
        findViewById<TextView>(R.id.month_view_title).apply {
            text = getString(R.string.month_view_title, getMonthText(month), year.toString())
        }
        findViewById<View>(R.id.add_task_btn).setOnClickListener { addTaskDialog() }

        val calendarView = findViewById<CalendarView>(R.id.calender_view)
        calendarView.setDaySelectedListener {
            if (it != -1) viewModel.setSelectedDay(year, month, it)
        }
        findDaysAndStartDay(year, month).also {
            viewModel.setSelectedDay(year, month, 1)
            calendarView.setMonthData(it.first, if (it.second + 1 == 8) 1 else it.second + 1, 1)
        }
        setUpTaskView()
    }

    private fun setUpTaskView() {
        tasksView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        taskAdapter = TaskAdapter(this) { viewModel.deleteTask(it) }
        tasksView.adapter = taskAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getTasks().collect {
                    taskAdapter.setData(it)
                }
            }
        }
    }

    private fun addTaskDialog() {
        dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.task_input_dialog)
        dialog.show()

        val taskNameLayout = dialog.findViewById<TextInputLayout>(R.id.task_name_layout)
        val taskNameInput = dialog.findViewById<TextInputEditText>(R.id.task_name)
        val taskDescInput = dialog.findViewById<TextInputEditText>(R.id.task_desc)

        val errorText = getString(R.string.task_name_required)
        taskNameInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                taskNameLayout?.error = if (s.isNullOrBlank()) errorText else null
            }
        })

        dialog.findViewById<View>(R.id.submit_task_btn)?.setOnClickListener {
            val taskName = taskNameInput?.text?.toString() ?: ""
            val taskDesc = taskDescInput?.text?.toString() ?: ""
            if (taskName.isNotBlank()) {
                viewModel.addTask(taskName, taskDesc)
                dialog.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::dialog.isInitialized && dialog.isShowing) dialog.dismiss()
    }

    companion object {
        const val KEY_YEAR = "year"
        const val KEY_MONTH = "month"
    }
}