package com.nam.android.svc.lotto.ui.tabs.canditates

import com.nam.android.svc.lotto.ui.dialog.remove.RemoveDialog
import com.nam.android.svc.lotto.ui.dialog.remove.RemoveDialogListener
import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.core.screen.SvcFragment

/**
 * @author bs.nam@navercorp.com
 */
class CandidatesFragment : SvcFragment<CandidatesViews, CandidatesControlTower>() {

    override fun createControlTower() = CandidatesControlTower(this, views)
    override fun createViews() = CandidatesViews()

    fun showMemberDialog(ball: Ball) {
        val dialog = RemoveDialog.newInstance(ball.number)
        dialog.dialogListener = object : RemoveDialogListener {
            override fun onClickRemoveBall() {
                controlTower.removeBall(ball)
            }
        }
        showDialog(dialog)
    }
}