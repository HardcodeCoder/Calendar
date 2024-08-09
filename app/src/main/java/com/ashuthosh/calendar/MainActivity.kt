package com.ashuthosh.calendar

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ashuthosh.calendar.monthview.MonthActivity
import com.google.android.material.color.DynamicColors
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var yearLayout: TextInputLayout
    private lateinit var yearInput: TextInputEditText
    private lateinit var monthLayout: TextInputLayout
    private lateinit var monthInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        DynamicColors.applyToActivityIfAvailable(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        yearLayout = findViewById(R.id.input_year_layout)
        yearInput = findViewById(R.id.input_year)
        monthLayout = findViewById(R.id.input_month_layout)
        monthInput = findViewById(R.id.input_month)

        val yearErrorText = getString(R.string.error_input_year)
        val monthErrorText = getString(R.string.error_input_month)

        yearInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isYearValid()) yearLayout.error = null
                else yearLayout.error = yearErrorText
            }
        })
        monthInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isMonthValid()) monthLayout.error = null
                else monthLayout.error = monthErrorText
            }
        })

        findViewById<View>(R.id.done_btn).setOnClickListener {
            if (isMonthValid() && isYearValid()) launchMonthView()
        }
    }

    private fun launchMonthView() {
        val intent = Intent(this, MonthActivity::class.java)
        intent.putExtra(MonthActivity.KEY_YEAR, yearInput.text.toString().toInt())
        intent.putExtra(MonthActivity.KEY_MONTH, monthInput.text.toString().toInt())
        startActivity(intent)
    }

    private fun isMonthValid(): Boolean {
        val month = monthInput.text?.toString()?.toIntOrNull() ?: 0
        return month in 1..12
    }

    private fun isYearValid(): Boolean {
        val year = yearInput.text?.toString()?.toIntOrNull() ?: 0
        return year > 0
    }
}