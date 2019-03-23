package com.nam.android.svc.lotto.ui.tabs.selections

import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.annotation.RequireControlTower
import com.naver.android.svc.annotation.RequireViews
import com.naver.android.svc.annotation.SvcFragment
import com.naver.android.svc.svcpeoplelotto.ui.dialog.member.RemoveDialog
import com.naver.android.svc.svcpeoplelotto.ui.dialog.member.RemoveDialogListener

/**
 * @author bs.nam@navercorp.com
 */
@SvcFragment
@RequireViews(CandidatesViews::class)
@RequireControlTower(CandidatesControlTower::class)
class CandidatesFragment : SVC_CandidatesFragment() {

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