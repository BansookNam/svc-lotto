package com.nam.android.svc.lotto.ui.tabs.selections

import com.naver.android.svc.core.screen.SvcFragment

/**
 * @author bs.nam@navercorp.com
 */
class SelectionsFragment : SvcFragment<SelectionsViews, SelectionsControlTower>() {

    override fun createControlTower() = SelectionsControlTower(this, views)
    override fun createViews() = SelectionsViews()
}