package com.example.findit.presentation.mappper

import com.example.findit.domain.model.ChatMessage
import com.example.findit.presentation.model.ChatMessagePresentation

import java.text.SimpleDateFormat
import java.util.*

fun ChatMessage.toPresentation(): ChatMessagePresentation {

    return ChatMessagePresentation(
        id = id,
        senderId = senderId,
        message = message,
        timestamp = formatTimestamp(timestamp)

    )
}
private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}