package com.nam.android.svc.lotto.ui.dialog.remove

import android.os.Bundle
import android.view.View
import com.nam.android.svc.lotto.BuildConfig
import com.naver.android.svc.annotation.RequireControlTower
import com.naver.android.svc.annotation.RequireListener
import com.naver.android.svc.annotation.RequireViews
import com.naver.android.svc.annotation.SvcDialogFragment
import com.naver.android.svc.core.controltower.EmptyControlTower

/**
 * @author bs.nam@navercorp.com
 */
@SvcDialogFragment
@RequireViews(RemoveViews::class)
@RequireControlTower(EmptyControlTower::class)
@RequireListener(RemoveDialogListener::class)
class RemoveDialog : SVC_RemoveDialog() {

    override val isFullScreenSupport = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val number = arguments?.get(KEY_NUMBER) as Int
        views.setNumber(number)

    }

    companion object {
        const val KEY_NUMBER = BuildConfig.APPLICATION_ID + ".KEY_NUMBER"

        fun newInstance(number: Int): RemoveDialog {
            val dialog = RemoveDialog()
            val bundle = Bundle()
            bundle.putInt(KEY_NUMBER, number)
            dialog.arguments = bundle
            return dialog
        }
    }
}