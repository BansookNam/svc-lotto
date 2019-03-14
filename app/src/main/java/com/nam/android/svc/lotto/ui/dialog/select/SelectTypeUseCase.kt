package com.naver.android.svc.svcpeoplelotto.ui.dialog.select

import com.naver.android.svc.core.views.ViewsAction

/**
 * @author bs.nam@navercorp.com
 */
interface SelectTypeUseCase : ViewsAction {
    fun selectJustInTime(count: Int)
    fun selectSequentially(count: Int)
    fun selectWithMyThumb(count: Int)
}