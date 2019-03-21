package com.nam.android.svc.lotto.ui.tabs.selections

import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.ui.MainViewModel
import com.nam.android.svc.lotto.ui.tabs.BallViewsAction
import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.core.controltower.ControlTower

/**
 * @author bs.nam@navercorp.com
 */
class SelectionsControlTower(screen: SelectionsFragment, views: SelectionsViews) :
    ControlTower<SelectionsFragment, SelectionsViews>(screen, views),
    BallViewsAction {

    private val vm by lazy { ViewModelProviders.of(screen.activity!!).get(MainViewModel::class.java) }

    override fun onCreated() {
    }

    override fun onClickBall(member: Ball) {
        vm.selectedBall.value = member
    }
}