package com.naver.android.svc.svcpeoplelotto.ui.dialog.member

import com.nam.android.svc.lotto.R
import com.naver.android.svc.core.screen.Screen
import com.naver.android.svc.core.views.Views
import kotlinx.android.synthetic.main.dialog_remove.view.*

/**
 * @author bs.nam@navercorp.com
 */
class RemoveViews(screen: Screen<*, *>) : Views() {

    override val layoutResId = R.layout.dialog_remove

    private val dialog by lazy{screen as RemoveDialog }
    private val dialogListener by lazy{ dialog.dialogListener }

    override fun onCreated() {
        withRootView {
            btn_remove.setOnClickListener {
                /**
                 * you can cast Screen as owner Screen (in this case RemoveDialog)
                 * this is not recommended way, however if the logic is too simple
                 * so that we do not need to create CT(control tower) or ViewsAction (making class, interface is cost too)
                 * you can cast your screen like below and call the function directly
                 */
                dialogListener.onClickRemoveBall()
                dialog.dismiss()
            }
        }
    }

    fun setNumber(number: Int) {
        rootView.ball_num.text = number.toString()
    }
}