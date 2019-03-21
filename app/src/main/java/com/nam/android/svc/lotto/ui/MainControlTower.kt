package com.nam.android.svc.lotto.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.R
import com.nam.android.svc.lotto.ui.dialog.select.SelectDialogCreator
import com.nam.android.svc.lotto.vo.Ball
import com.nam.android.svc.lotto.vo.BallPool
import com.naver.android.annotation.ControlTower
import com.naver.android.annotation.RequireScreen
import com.naver.android.annotation.RequireViews
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
    RemoveBallController {

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

    override fun removeRandoms() {
        super.removeRandoms()
    }

    override suspend fun startRemovingSQ(delayTime: Long) {
        super.startRemovingSQ(delayTime)
    }

    override fun checkCandidatesCountNotValid(count: Int): Boolean {
        val listSize = vm.candidates.value?.size ?: 0
        return if (listSize < count) {
            showToast(R.string.candidate_count_is_smaller)
            true
        } else {
            false
        }
    }

    override fun finishRandom() {
        showToast("뽑기 완료!")
        type = null
        vm.selectCount = 0
        count = 0
    }

    fun onClickShuffle() {
        vm.candidates.value?.shuffle()
        vm.candidates.value = vm.candidates.value
    }

    fun onClickCopy() {
        val currentItem = views.getCurrentItem()

        val copyList = if (currentItem == 0) vm.candidates.value else vm.selections.value

        if (copyList?.size == 0) {
            when (currentItem) {
                0 -> showToast(R.string.cannot_copy_candidate)
                1 -> showToast(R.string.cannot_copy_winner)
            }
            return
        }

        var copyString = ""
        copyList?.forEach {
            copyString += it.number.toString() + ", "
        }

        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("members", copyString)
        clipboard.primaryClip = clip

        when (currentItem) {
            0 -> showToast(R.string.copy_complete_candidate)
            1 -> showToast(R.string.copy_complete_winner)
        }
    }

    fun onClickReload() {
        val selections: MutableList<Ball> = vm.selections.value ?: ArrayList()
        val candidates: MutableList<Ball> = vm.candidates.value ?: ArrayList()
        candidates.addAll(selections)
        vm.candidates.value = candidates
        vm.selections.value = ArrayList()
    }


}