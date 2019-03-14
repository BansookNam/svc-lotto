package com.nam.android.svc.lotto.ui.tabs.selections

import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.ui.MainViewModel
import com.naver.android.svc.core.screen.SvcFragment

/**
 * @author bs.nam@navercorp.com
 */
class SelectionsFragment : SvcFragment<SelectionsViews, SelectionsControlTower>() {

    override fun createControlTower() = SelectionsControlTower(this, views)
    override fun createViews() = SelectionsViews(ViewModelProviders.of(activity!!).get(MainViewModel::class.java))
}