package com.ashuthosh.calendar.monthview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.ashuthosh.calendar.R
import kotlin.math.pow
import kotlin.math.sqrt

class CalendarView : View {

    private val labels = arrayOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
    private val dayLayouts = arrayOfNulls<StaticLayout>(32)
    private val weekLayouts = arrayOfNulls<StaticLayout>(7)
    private val days = arrayOfNulls<Day>(32)
    private val textPaint: TextPaint
    private var onDaySelected: ((Int) -> Unit)? = null
    private var touchSlope = 0
    private var activeDayColor = 0
    private var rowSpacing = 0
    private var daySize = 0
    private var gapBetweenDays = 0
    private var numOfDays = 30
    private var startDay = 4
    private var selectedDay = 0

    constructor(context: Context) : this(context, null, 0, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        styleAttr: Int
    ) : this(context, attrs, styleAttr, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        styleAttr: Int,
        styleRes: Int
    ) : super(context, attrs, styleAttr, styleRes) {

        touchSlope = ViewConfiguration.get(context).scaledTouchSlop
        val customAttrs = context.obtainStyledAttributes(attrs, R.styleable.CalendarView)
        val defTextSize = customAttrs.getDimensionPixelSize(
            R.styleable.CalendarView_textSize,
            context.resources.getDimensionPixelSize(R.dimen.calendar_view_def_text_size)
        )
        rowSpacing = customAttrs.getDimensionPixelSize(
            R.styleable.CalendarView_rowSpacing,
            context.resources.getDimensionPixelSize(R.dimen.calendar_view_def_row_spacing)
        )
        gapBetweenDays = rowSpacing

        val arr = context.obtainStyledAttributes(
            intArrayOf(
                com.google.android.material.R.attr.colorPrimaryContainer,
                com.google.android.material.R.attr.colorOnPrimaryContainer,
            )
        )

        activeDayColor = arr.getColor(0, 0)

        textPaint = TextPaint().apply {
            isAntiAlias = true
            typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            textSize = defTextSize.toFloat()
            letterSpacing = 0.06f
            color = arr.getColor(1, Color.BLACK)
        }
        customAttrs.recycle()
        arr.recycle()
    }

    fun setMonthData(days: Int, start: Int, selected: Int) {
        numOfDays = days
        startDay = start
        selectedDay = selected
        reset()
    }

    fun setSelectedDay(day: Int) {
        selectedDay = day
        invalidate()
    }

    fun setDaySelectedListener(listener: (Int) -> Unit) {
        onDaySelected = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        daySize = (width - paddingStart - paddingEnd - (6 * gapBetweenDays)) / 7
        val x = 0f + paddingStart
        var startX = x
        var startY = 0f + paddingTop

        textPaint.isFakeBoldText = true
        labels.forEachIndexed { index, _ ->
            drawWeekLabel(canvas, startX, startY, index)
            startX += daySize + gapBetweenDays
        }

        startX = x
        startY += daySize + rowSpacing
        textPaint.isFakeBoldText = false

        var itemsInRow = startDay - 1
        startX += itemsInRow * (daySize + gapBetweenDays)

        for (day in 1..numOfDays) {
            drawDay(canvas, startX, startY, day)
            itemsInRow++
            if (itemsInRow % 7 == 0) {
                startX = x
                startY += daySize + rowSpacing
            } else startX += daySize + gapBetweenDays
        }
    }

    private var touchDownX = 0f
    private var touchDownY = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownY = event.y
                touchDownX = event.x
                return true
            }

            MotionEvent.ACTION_UP -> {
                val dx = (event.x - touchDownX).toDouble()
                val dy = (event.y - touchDownY).toDouble()
                val d = sqrt(dx.pow(2) + dy.pow(2)).toInt()
                if (d < touchSlope) {
                    val day = findDayFromCoordinates(event.x, event.y)
                    onDaySelected?.let { it(day) }
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun drawWeekLabel(canvas: Canvas, x: Float, y: Float, pos: Int) {
        canvas.save()
        val layout = getWeekLayout(pos)
        canvas.translate(x, y + layout.height / 2f)
        layout.draw(canvas)
        canvas.restore()
    }

    private fun drawDay(canvas: Canvas, x: Float, y: Float, day: Int) {
        if (days[day] == null) {
            days[day] = Day(day, x, x + daySize, y, y + daySize)
        }
        canvas.save()
        if (day == selectedDay) {
            canvas.clipRect(x, y, x + daySize, y + daySize)
            val r = Color.red(activeDayColor)
            val g = Color.green(activeDayColor)
            val b = Color.blue(activeDayColor)
            canvas.drawARGB(255, r, g, b)
        }
        val layout = getDayLayout(day)
        canvas.translate(x, y + layout.height / 2f)
        layout.draw(canvas)
        canvas.restore()
    }

    private fun getDayLayout(day: Int): StaticLayout {
        if (dayLayouts[day] == null) dayLayouts[day] = getNewLayout(day.toString())
        return dayLayouts[day]!!
    }

    private fun getWeekLayout(week: Int): StaticLayout {
        if (weekLayouts[week] == null) weekLayouts[week] = getNewLayout(labels[week])
        return weekLayouts[week]!!
    }

    private fun getNewLayout(text: String) = StaticLayout.Builder.obtain(
        text,
        0,
        text.length,
        textPaint,
        daySize
    ).setAlignment(Layout.Alignment.ALIGN_CENTER).build()

    private fun findDayFromCoordinates(x: Float, y: Float): Int {
        return days.filterNotNull()
                   .firstOrNull { x in it.startX..it.endX && y in it.startY..it.endY }?.day ?: -1
    }

    private fun reset() {
        dayLayouts.fill(null)
        weekLayouts.fill(null)
        days.fill(null)
        invalidate()
    }

    private data class Day(
        val day: Int,
        val startX: Float,
        val endX: Float,
        val startY: Float,
        val endY: Float
    )
}