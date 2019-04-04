package com.nam.android.svc.lotto.vo

import androidx.annotation.ColorInt

data class Ball
constructor(
    val number: Int,
    @ColorInt val color: Int
)

