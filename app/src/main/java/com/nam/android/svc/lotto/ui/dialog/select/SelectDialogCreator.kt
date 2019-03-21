package com.nam.android.svc.lotto.ui.dialog.select

import com.nam.android.svc.lotto.R
import com.nam.android.svc.lotto.ui.MainViewModel
import com.naver.android.svc.svcpeoplelotto.ui.dialog.select.SelectMode
import com.naver.android.svc.svcpeoplelotto.ui.dialog.select.SelectTypeDialogListener
import com.navercorp.android.selective.tools.launch
import kotlinx.coroutines.Job

/**
 * @author bs.nam@navercorp.com
 */
interface SelectDialogCreator {

    var type: SelectMode?
    var count: Int
    var job: Job?
    val vm: MainViewModel

    fun createDialogListener():SelectTypeDialogListener {
        return object : SelectTypeDialogListener {
            override fun selectJustInTime(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }

                type = SelectMode.JIT
                vm.selectCount = count
                job = launch {
                    removeRandoms()
                }
            }

            override fun selectSequentially(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.SQ
                vm.selectCount = count
                job = launch {
                    startRemovingSQ(800)
                }
            }

            override fun selectWithMyThumb(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.THUMB
                vm.selectCount = count
            }
        }
    }

    fun checkCandidatesCountNotValid(count: Int): Boolean {
        val listSize = vm.candidates.value?.size ?: 0
        return if (listSize < count) {
            showToast(R.string.candidate_count_is_smaller)
            true
        } else {
            false
        }
    }

    fun showToast(resId: Int)
    fun removeRandoms()
    suspend fun startRemovingSQ(delayTime: Long)
}