package com.nam.android.svc.lotto.ui.dialog.select

import com.nam.android.svc.lotto.R
import com.naver.android.svc.core.controltower.ControlTower

/**
 * @author bs.nam@navercorp.com
 */
class SelectTypeControlTower(screen: SelectTypeDialog, views: SelectTypeViews) :
    ControlTower<SelectTypeDialog, SelectTypeViews>(screen, views),
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