package com.nam.android.svc.lotto.ui.dialog.select

import com.naver.android.svc.core.screen.SvcDialogFragment

/**
 * @author bs.nam@navercorp.com
 */

class SelectTypeDialog : SvcDialogFragment<SelectTypeViews, SelectTypeControlTower, SelectTypeDialogListener>() {

    override fun createControlTower() = SelectTypeControlTower(this, views)
    override fun createViews() = SelectTypeViews()

    override val isFullScreenSupport = true
}