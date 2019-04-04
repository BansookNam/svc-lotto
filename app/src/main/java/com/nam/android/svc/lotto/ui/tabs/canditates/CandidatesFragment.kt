package com.nam.android.svc.lotto.ui.tabs.canditates

import com.nam.android.svc.lotto.ui.dialog.remove.RemoveDialog
import com.nam.android.svc.lotto.ui.dialog.remove.RemoveDialogListener
import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.annotation.RequireControlTower
import com.naver.android.svc.annotation.RequireViews
import com.naver.android.svc.annotation.SvcFragment

/**
 * @author bs.nam@navercorp.com
 */
@SvcFragment
@RequireViews(CandidatesViews::class)
@RequireControlTower(CandidatesControlTower::class)
class CandidatesFragment : SVC_CandidatesFragment() {

    fun showRemoveDialog(ball: Ball) {
        val dialog = RemoveDialog.newInstance(ball.number, ball.color)
        dialog.dialogListener = object : RemoveDialogListener {
            override fun onClickRemoveBall() {
                controlTower.removeBall(ball)
            }
        }
        showDialog(dialog)
    }
}

