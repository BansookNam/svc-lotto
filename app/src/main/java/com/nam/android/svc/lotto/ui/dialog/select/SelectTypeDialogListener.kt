package com.nam.android.svc.lotto.ui.dialog.select

import com.naver.android.svc.core.views.ViewsAction

/**
 * @author bs.nam@navercorp.com
 */
interface SelectTypeDialogListener : ViewsAction {
    fun selectJustInTime(count: Int)
    fun selectSequentially(count: Int)
    fun selectWithMyThumb(count: Int)
}