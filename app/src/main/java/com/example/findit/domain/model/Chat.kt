package com.example.findit.domain.model


data class Chat(
    val id: String = "",
    val participantIds: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastTimestamp: Long = 0L
)




