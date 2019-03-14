package com.nam.android.svc.lotto.ui.tabs

import android.graphics.PorterDuff
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.view.ViewGroup
import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.recyclerview.ActionHolder
import kotlinx.android.synthetic.main.item_ball.view.*

class BallViewHolder(layoutId: Int, parent: ViewGroup, listUseCase: BallViewsAction) :
    ActionHolder<Ball, BallViewsAction>(layoutId, parent, listUseCase) {
    val ballNum = itemView.ball_num


    override fun bindView(item: Ball, position: Int) {

        ballNum.background = ShapeDrawable(OvalShape()).apply {
            setColorFilter(item.color, PorterDuff.Mode.SRC_IN)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ballNum.clipToOutline = true
        }
        ballNum.text = item.number.toString()
        itemView.setOnClickListener {
            action.onClickBall(item)
        }
    }

}