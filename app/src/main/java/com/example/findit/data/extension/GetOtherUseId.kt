package com.example.findit.data.extension

fun String.getOtherUserId(myId: String): String {
    val ids = this.split("_")
    return ids.firstOrNull { it != myId } ?: ""
}