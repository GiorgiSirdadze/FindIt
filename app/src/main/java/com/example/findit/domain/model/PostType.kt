package com.example.findit.domain.model

enum class PostType {
    FOUND {
        override fun toString(): String {
            return "FOUND"
        }
    },
    LOST {
        override fun toString(): String {
            return "LOST"
        }
    };

    companion object {
        fun fromString(value: String): PostType {
            return when (value.uppercase()) {
                "FOUND" -> FOUND
                else -> LOST
            }
        }
    }
}
