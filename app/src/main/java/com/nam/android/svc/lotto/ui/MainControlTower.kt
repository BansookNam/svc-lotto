package com.nam.android.svc.lotto.ui

import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.ui.controller.ClipBoardCopyController
import com.nam.android.svc.lotto.ui.controller.ReloadController
import com.nam.android.svc.lotto.ui.controller.RemoveBallController
import com.nam.android.svc.lotto.ui.controller.ShuffleController
import com.nam.android.svc.lotto.ui.dialog.select.SelectDialogCreator
import com.nam.android.svc.lotto.ui.dialog.select.SelectMode
import com.nam.android.svc.lotto.vo.BallPool
import com.naver.android.svc.annotation.ControlTower
import com.naver.android.svc.annotation.RequireScreen
import com.naver.android.svc.annotation.RequireViews
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
    ClipBoardCopyController,
    ShuffleController,
    ReloadController{

    override var type: SelectMode? = null
    override var count = 0
    override var removeJob: Job? = null
    override var isShuffling: Boolean = false
    override var shufflingJob: Job? = null

    override val vm by lazy { ViewModelProviders.of(screen).get(MainViewModel::class.java) }

    val selectTypeListener = createDialogListener()

    override fun onCreated() {
        vm.candidates.value = BallPool.createBalls()
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
        }
    }

    fun onClickShuffle() {
        toggleShuffle()
    }

    fun onClickCopy() {
        copy()
    }

    fun onClickReload() {
        reload()
    }

    override fun removeRandoms() {
        super.removeRandoms()
    }

    override suspend fun startRemovingSQ(delayTime: Long) {
        super.startRemovingSQ(delayTime)
    }
}