package com.nam.android.svc.lotto.ui

import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.ui.controller.ClipBoardCopyController
import com.nam.android.svc.lotto.ui.controller.RemoveBallController
import com.nam.android.svc.lotto.ui.dialog.select.SelectDialogCreator
import com.nam.android.svc.lotto.vo.Ball
import com.nam.android.svc.lotto.vo.BallPool
import com.naver.android.svc.annotation.ControlTower
import com.naver.android.svc.annotation.RequireScreen
import com.naver.android.svc.annotation.RequireViews
import com.naver.android.svc.svcpeoplelotto.ui.dialog.select.SelectMode
import kotlinx.coroutines.Job
import java.util.*

/**
 * @author bs.nam@navercorp.com
 */

@ControlTower
@RequireScreen(MainActivity::class)
@RequireViews(MainViews::class)
class MainControlTower : SVC_MainControlTower(),
    MainViewsAction,
    SelectDialogCreator,
    RemoveBallController,
    ClipBoardCopyController {

    override var type: SelectMode? = null
    override var count = 0
    override var job: Job? = null

    override val vm by lazy { ViewModelProviders.of(screen).get(MainViewModel::class.java) }

    val selectTypeListener = createDialogListener()

    override fun onCreated() {
        val candidates = BallPool.createBalls()

        vm.candidates.value = candidates
        vm.selections.value = ArrayList()

        vm.selections.observe(screen, androidx.lifecycle.Observer {
            screen.setReloadMenuItemVisible(it?.size != 0)
        })
    }

    override fun onClickSelect() {
        if (type == null) {
            screen.showSelectTypeDialog()
            return
        }

        when (type) {
            SelectMode.JIT -> return
            SelectMode.SQ -> return
            SelectMode.THUMB -> removeRandomOne()
            null -> screen.showSelectTypeDialog()
        }
    }

    fun onClickShuffle() {
        vm.candidates.value?.shuffle()
        vm.candidates.value = vm.candidates.value
    }

    fun onClickCopy() {
        super.copy()
    }

    fun onClickReload() {
        val selections: MutableList<Ball> = vm.selections.value ?: ArrayList()
        val candidates: MutableList<Ball> = vm.candidates.value ?: ArrayList()
        candidates.addAll(selections)
        vm.candidates.value = candidates
        vm.selections.value = ArrayList()
    }

    override fun removeRandoms() {
        super.removeRandoms()
    }

    override suspend fun startRemovingSQ(delayTime: Long) {
        super.startRemovingSQ(delayTime)
    }
}