package com.nam.android.svc.lotto.ui.dialog.select

import com.nam.android.svc.lotto.R
import com.naver.android.svc.annotation.ControlTower
import com.naver.android.svc.annotation.RequireListener
import com.naver.android.svc.annotation.RequireScreen
import com.naver.android.svc.annotation.RequireViews

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
        showToast(R.string.click_pick)
        screen.dismiss()
    }

}