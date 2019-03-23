package com.nam.android.svc.lotto.ui

import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.ui.controller.ClipBoardCopyController
import com.nam.android.svc.lotto.ui.controller.RemoveBallController
import com.nam.android.svc.lotto.ui.controller.ShuffleController
import com.nam.android.svc.lotto.ui.dialog.select.SelectDialogCreator
import com.nam.android.svc.lotto.ui.dialog.select.SelectMode
import com.nam.android.svc.lotto.vo.Ball
import com.nam.android.svc.lotto.vo.BallPool
import com.naver.android.svc.core.controltower.ControlTower
import kotlinx.coroutines.Job
import java.util.*

/**
 * @author bs.nam@navercorp.com
 */

class MainControlTower(screen: MainActivity, views: MainViews) : ControlTower<MainActivity, MainViews>(screen, views),
    MainViewsAction,
    SelectDialogCreator,
    RemoveBallController,
    ClipBoardCopyController,
    ShuffleController {

    override var type: SelectMode? = null
    override var count = 0
    override var removeJob: Job? = null
    override var isShuffling: Boolean = false
    override var shufflingJob: Job? = null

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
        toggleShuffle()
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