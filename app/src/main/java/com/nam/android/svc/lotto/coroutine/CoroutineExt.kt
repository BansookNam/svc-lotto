/*
 * @(#)CoroutineExt.kt 2018. 11. 23.
 * Copyright 2018 NAVER Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.navercorp.android.selective.tools

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author bs.nam@navercorp.com
 */
fun launch(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Default).launch(block = block)
}

fun ui(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(UI).launch(block = block)
}

val UI = Dispatchers.Main