package com.example.findit.data.mapper

import com.example.findit.domain.model.ChatMessage

fun ChatMessage.toMap() = mapOf(
    "id" to id,
    "senderId" to senderId,
    "message" to message,
    "timestamp" to timestamp
)