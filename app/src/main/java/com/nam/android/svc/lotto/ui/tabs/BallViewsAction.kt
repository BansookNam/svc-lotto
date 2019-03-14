package com.nam.android.svc.lotto.ui.tabs

import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.core.views.ViewsAction

interface BallViewsAction : ViewsAction {
    fun onClickBall(member: Ball)
}