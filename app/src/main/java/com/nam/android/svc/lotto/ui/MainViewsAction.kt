package com.nam.android.svc.lotto.ui

import com.naver.android.svc.core.views.ViewsAction

/**
 * @author bs.nam@navercorp.com
 */
interface MainViewsAction:ViewsAction{
    fun onViewPagerTouchUp()
    fun onClickRandom()
}