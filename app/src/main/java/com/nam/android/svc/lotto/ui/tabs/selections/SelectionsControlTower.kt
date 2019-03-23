package com.nam.android.svc.lotto.ui.tabs.selections

import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.ui.MainViewModel
import com.nam.android.svc.lotto.ui.tabs.BallViewsAction
import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.annotation.ControlTower
import com.naver.android.svc.annotation.RequireScreen
import com.naver.android.svc.annotation.RequireViews

/**
 * @author bs.nam@navercorp.com
 */
@ControlTower
@RequireScreen(SelectionsFragment::class)
@RequireViews(SelectionsViews::class)
class SelectionsControlTower : SVC_SelectionsControlTower(),
    BallViewsAction {

    private val vm by lazy { ViewModelProviders.of(screen.activity!!).get(MainViewModel::class.java) }

    override fun onCreated() {
    }

    override fun onClickBall(member: Ball) {
        vm.selectedBall.value = member
    }
}