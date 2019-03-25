package com.nam.android.svc.lotto.ui.dialog.select

import com.nam.android.svc.lotto.ui.MainViewModel
import kotlinx.coroutines.Job

/**
 * @author bs.nam@navercorp.com
 */
interface SelectDialogCreator {

    var type: SelectMode?
    var count: Int
    var removeJob: Job?
    val vm: MainViewModel


    fun showToast(resId: Int)
    fun removeRandoms()
    suspend fun startRemovingSQ(delayTime: Long)
}