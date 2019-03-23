package com.naver.android.svc.svcpeoplelotto.ui.dialog.select

import com.nam.android.svc.lotto.ui.dialog.select.SelectTypeControlTower
import com.naver.android.svc.annotation.RequireControlTower
import com.naver.android.svc.annotation.RequireListener
import com.naver.android.svc.annotation.RequireViews

/**
 * @author bs.nam@navercorp.com
 */

@com.naver.android.svc.annotation.SvcDialogFragment
@RequireViews(SelectTypeViews::class)
@RequireControlTower(SelectTypeControlTower::class)
@RequireListener(SelectTypeDialogListener::class)
class SelectTypeDialog : SVC_SelectTypeDialog() {

    override val isFullScreenSupport = true
}