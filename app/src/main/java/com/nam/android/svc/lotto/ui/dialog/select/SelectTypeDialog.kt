package com.naver.android.svc.svcpeoplelotto.ui.dialog.select

import com.nam.android.svc.lotto.ui.dialog.select.SelectTypeControlTower
import com.naver.android.annotation.RequireControlTower
import com.naver.android.annotation.RequireListener
import com.naver.android.annotation.RequireViews

/**
 * @author bs.nam@navercorp.com
 */

@com.naver.android.annotation.SvcDialogFragment
@RequireViews(SelectTypeViews::class)
@RequireControlTower(SelectTypeControlTower::class)
@RequireListener(SelectTypeDialogListener::class)
class SelectTypeDialog : SVC_SelectTypeDialog() {

    override val isFullScreenSupport = true
}