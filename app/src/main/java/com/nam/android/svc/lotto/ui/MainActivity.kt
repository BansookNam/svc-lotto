package com.nam.android.svc.lotto.ui

import com.naver.android.svc.core.screen.SvcActivity

class MainActivity : SvcActivity<MainViews, MainControlTower>() {
    override fun createControlTower() = MainControlTower(this, views)
    override fun createViews() = MainViews()

}
