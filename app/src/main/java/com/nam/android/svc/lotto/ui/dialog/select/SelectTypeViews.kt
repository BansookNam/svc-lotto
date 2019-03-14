package com.naver.android.svc.svcpeoplelotto.ui.dialog.select

import com.nam.android.svc.lotto.R
import com.naver.android.svc.core.views.ActionViews
import kotlinx.android.synthetic.main.dialog_select_type.view.*

/**
 * @author bs.nam@navercorp.com
 */
class SelectTypeViews : ActionViews<SelectTypeDialogListener>() {

    override val layoutResId = R.layout.dialog_select_type

    override fun onCreated() {
        withRootView {
            select_just_in_time.setOnClickListener {
                if (checkNumber()) {
                    return@setOnClickListener
                }
                viewsAction.selectJustInTime(number)
            }

            select_sq.setOnClickListener {
                if (checkNumber()) {
                    return@setOnClickListener
                }
                viewsAction.selectSequentially(number)
            }

            select_thumb.setOnClickListener {
                if (checkNumber()) {
                    return@setOnClickListener
                }
                viewsAction.selectWithMyThumb(number)
            }
        }
    }

    private fun checkNumber() = rootView.select_count_goal.text.toString().isEmpty()

    private val number
        get() = rootView.select_count_goal.text.toString().toInt()
}