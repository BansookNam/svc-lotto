package com.nam.android.svc.lotto.ui.tabs.selections

import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.R
import com.nam.android.svc.lotto.ui.MainViewModel
import com.nam.android.svc.lotto.ui.tabs.BallViewsAction
import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.core.controltower.ControlTower

/**
 * @author bs.nam@navercorp.com
 */
class CandidatesControlTower(screen: CandidatesFragment, views: CandidatesViews) :
    ControlTower<CandidatesFragment, CandidatesViews>(screen, views),
    BallViewsAction {

    val vm = ViewModelProviders.of(screen.activity!!).get(MainViewModel::class.java)

    override fun onCreated() {
    }

    override fun onClickBall(member: Ball) {
        screen.showMemberDialog(member)
    }

    fun removeBall(ball: Ball) {
        val candidates = vm.candidates.value?.size ?: 0
        if (candidates <= vm.selectCount) {
            showToast(R.string.cannot_remove_candidate_count_is_smaller)
            return
        }

        vm.candidates.value?.remove(ball)
        vm.candidates.value = vm.candidates.value
    }
}