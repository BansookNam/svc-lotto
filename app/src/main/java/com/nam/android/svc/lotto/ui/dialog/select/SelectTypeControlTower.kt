package com.nam.android.svc.lotto.ui.dialog.select

import com.naver.android.annotation.ControlTower
import com.naver.android.annotation.RequireListener
import com.naver.android.annotation.RequireScreen
import com.naver.android.annotation.RequireViews
import com.naver.android.svc.svcpeoplelotto.ui.dialog.select.SelectTypeDialog
import com.naver.android.svc.svcpeoplelotto.ui.dialog.select.SelectTypeDialogListener
import com.naver.android.svc.svcpeoplelotto.ui.dialog.select.SelectTypeViews

/**
 * @author bs.nam@navercorp.com
 */
@ControlTower
@RequireScreen(SelectTypeDialog::class)
@RequireViews(SelectTypeViews::class)
@RequireListener(SelectTypeDialogListener::class)
class SelectTypeControlTower : SVC_SelectTypeControlTower(),
    SelectTypeDialogListener {

    override fun onCreated() {
    }

    override fun selectJustInTime(count: Int) {
        screen.dialogListener.selectJustInTime(count)
        screen.dismiss()
    }

    override fun selectSequentially(count: Int) {
        screen.dialogListener.selectSequentially(count)
        screen.dismiss()
    }

    override fun selectWithMyThumb(count: Int) {
        screen.dialogListener.selectWithMyThumb(count)
        screen.dismiss()
    }

}