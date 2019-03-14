package com.naver.android.svc.svcpeoplelotto.ui.dialog.member

import android.os.Bundle
import android.view.View
import com.nam.android.svc.lotto.BuildConfig
import com.naver.android.svc.core.controltower.EmptyControlTower
import com.naver.android.svc.core.screen.SvcDialogFragment

/**
 * @author bs.nam@navercorp.com
 */
class RemoveDialog : SvcDialogFragment<RemoveViews, EmptyControlTower<*,*>, RemoveDialogListener>() {

    override fun createControlTower() = EmptyControlTower(this, views)
    override fun createViews() = RemoveViews(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val number = arguments?.get(KEY_NUMBER) as Int
        views.setNumber(number)

    }

    companion object {
        val KEY_NUMBER = BuildConfig.APPLICATION_ID + ".KEY_NUMBER"

        fun newInstance(number: Int): RemoveDialog {
            val dialog = RemoveDialog()
            val bundle = Bundle()
            bundle.putInt(KEY_NUMBER, number)
            dialog.arguments = bundle
            return dialog
        }
    }
}