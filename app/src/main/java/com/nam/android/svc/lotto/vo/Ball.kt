package com.nam.android.svc.lotto.vo

import androidx.annotation.ColorInt

data class Ball
@JvmOverloads
constructor(
    val number: Int,
    @ColorInt val color: Int
)