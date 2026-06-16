package com.vrsalex.taskflow.presentation.common.extension

import androidx.annotation.StringRes
import com.vrsalex.taskflow.R
import kotlinx.datetime.Month


private val MONTH_NAMES = intArrayOf(
    R.string.month_jan, R.string.month_feb, R.string.month_mar, R.string.month_apr,
    R.string.month_may, R.string.month_jun, R.string.month_jul, R.string.month_aug,
    R.string.month_sep, R.string.month_oct, R.string.month_nov, R.string.month_dec,
)

@StringRes fun Month.monthNameRes(): Int = MONTH_NAMES[ordinal]
