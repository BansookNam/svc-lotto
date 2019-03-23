package com.nam.android.svc.lotto.ui.controller

import com.nam.android.svc.lotto.ui.MainViewModel
import com.nam.android.svc.lotto.vo.Ball
import com.nam.android.svc.lotto.vo.BallPool
import java.util.*

/**
 * @author bs.nam@navercorp.com
 */
interface ReloadController {
    val vm: MainViewModel
    val views: IndexProvideViews

    fun reload() {
        val currentItem = views.getCurrentItem()
        if (currentItem == 0) {
            createNewBalls()
        } else {
            moveSelectionsToCandidates()
        }
    }

    private fun createNewBalls() {
        vm.candidates.value = BallPool.createBalls()
        vm.selections.value = ArrayList()
    }

    private fun moveSelectionsToCandidates() {
        val selections: MutableList<Ball> = vm.selections.value ?: ArrayList()
        val candidates: MutableList<Ball> = vm.candidates.value ?: ArrayList()
        candidates.addAll(selections)
        vm.candidates.value = candidates
        vm.selections.value = ArrayList()
    }
}