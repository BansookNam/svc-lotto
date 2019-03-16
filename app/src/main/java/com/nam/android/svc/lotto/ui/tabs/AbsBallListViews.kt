package com.nam.android.svc.lotto.ui.tabs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nam.android.svc.lotto.R
import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.core.views.ActionViews
import kotlinx.android.synthetic.main.fragment_ball_list.view.*

/**
 * @author bs.nam@navercorp.com
 */
abstract class AbsBallListViews : ActionViews<BallViewsAction>() {

    override val layoutResId = R.layout.fragment_ball_list

    val adapter: BallAdapter by lazy { BallAdapter(viewsAction) }

    abstract val ballList : MutableLiveData<MutableList<Ball>>

    override fun onCreated() {
        withRootView {
            rv_members.adapter = adapter

            ballList.observe(screen, Observer {
                if (it == null) {
                    return@Observer
                }
                adapter.updateList(ArrayList(it))
            })
        }
    }


}