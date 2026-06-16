package com.vrsalex.taskflow.domain.note.base

import androidx.compose.ui.graphics.Color
import com.vrsalex.taskflow.R
import com.vrsalex.uikit.theme.Priority1
import com.vrsalex.uikit.theme.Priority2
import com.vrsalex.uikit.theme.Priority3

enum class NotePriority(val color: Color?, val title: Int, val value: Short) {
    P1(Priority1, com.vrsalex.uikit.R.string.priority1, 1),
    P2(Priority2, com.vrsalex.uikit.R.string.priority2, 2),
    P3(Priority3, com.vrsalex.uikit.R.string.priority3, 3),
    P0(null, com.vrsalex.uikit.R.string.priority0, 0);

    val sortRank: Int get() = if (this == P0) Int.MAX_VALUE else value.toInt()
}