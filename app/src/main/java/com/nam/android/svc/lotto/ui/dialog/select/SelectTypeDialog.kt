package com.nam.android.svc.lotto.ui.dialog.select

import com.naver.android.svc.annotation.RequireControlTower
import com.naver.android.svc.annotation.RequireListener
import com.naver.android.svc.annotation.RequireViews
import com.naver.android.svc.annotation.SvcDialogFragment

/**
 * @author bs.nam@navercorp.com
 */

@SvcDialogFragment
@RequireViews(SelectTypeViews::class)
@RequireControlTower(SelectTypeControlTower::class)
@RequireListener(SelectTypeDialogListener::class)
class SelectTypeDialog : SVC_SelectTypeDialog() {

    override val isFullScreenSupport = true
}