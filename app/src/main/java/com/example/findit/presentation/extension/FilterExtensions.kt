package com.example.findit.presentation.extension

import com.example.findit.presentation.model.PostPresentation
import com.example.findit.presentation.model.PostPresentationFilter
import java.util.Calendar

fun PostPresentationFilter.isLost(): Boolean {
    return this.postType.name.equals("LOST", ignoreCase = true)
}

fun PostPresentationFilter.isFound(): Boolean {
    return this.postType.name.equals("FOUND", ignoreCase = true)
}

fun PostPresentationFilter.isToday(): Boolean {
    val postCalendar = getPostCalendar()
    val today = Calendar.getInstance()
    return postCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            postCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
}

fun PostPresentationFilter.isLastWeek(): Boolean {
    val postCalendar = getPostCalendar()
    val today = Calendar.getInstance()

    today.add(Calendar.WEEK_OF_YEAR, -1)

    return postCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            postCalendar.get(Calendar.WEEK_OF_YEAR) == today.get(Calendar.WEEK_OF_YEAR)
}


fun PostPresentationFilter.isLastMonth(): Boolean {
    val postCalendar = getPostCalendar()
    val today = Calendar.getInstance()

    today.add(Calendar.MONTH, -1)

    return postCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            postCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
}


private fun PostPresentationFilter.getPostCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.timestamp
    return calendar
}
