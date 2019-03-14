package com.nam.android.svc.lotto.ui.dialog.select

import com.naver.android.svc.core.controltower.ControlTower
import com.naver.android.svc.svcpeoplelotto.ui.dialog.select.SelectTypeDialog
import com.naver.android.svc.svcpeoplelotto.ui.dialog.select.SelectTypeUseCase
import com.naver.android.svc.svcpeoplelotto.ui.dialog.select.SelectTypeViews

/**
 * @author bs.nam@navercorp.com
 */
class SelectTypeControlTower(screen: SelectTypeDialog, views: SelectTypeViews) : ControlTower<SelectTypeDialog, SelectTypeViews>(screen, views),
    SelectTypeUseCase {

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