package com.example.findit.presentation.extension

import com.example.findit.R
import com.example.findit.domain.model.PostType

fun PostType.getIconRes(): Int {
    return when (this) {
        PostType.LOST -> R.drawable.lost_icon
        PostType.FOUND -> R.drawable.found_icon
    }
}