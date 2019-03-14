package com.naver.android.svc.svcpeoplelotto.ui.dialog.select

import com.nam.android.svc.lotto.ui.dialog.select.SelectTypeControlTower
import com.naver.android.svc.core.screen.SvcDialogFragment

/**
 * @author bs.nam@navercorp.com
 */
class SelectTypeDialog : SvcDialogFragment<SelectTypeViews, SelectTypeControlTower, SelectTypeUseCase>() {

    override fun createControlTower() = SelectTypeControlTower(this, views)
    override fun createViews() = SelectTypeViews()
}