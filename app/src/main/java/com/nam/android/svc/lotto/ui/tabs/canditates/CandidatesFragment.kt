package com.nam.android.svc.lotto.ui.tabs.selections

import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.ui.MainViewModel
import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.core.screen.SvcFragment
import com.naver.android.svc.svcpeoplelotto.ui.dialog.member.RemoveDialog
import com.naver.android.svc.svcpeoplelotto.ui.dialog.member.RemoveDialogListener

/**
 * @author bs.nam@navercorp.com
 */
class CandidatesFragment : SvcFragment<CandidatesViews, CandidatesControlTower>() {

    override fun createControlTower() = CandidatesControlTower(this, views)
    override fun createViews() = CandidatesViews(ViewModelProviders.of(activity!!).get(MainViewModel::class.java))

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