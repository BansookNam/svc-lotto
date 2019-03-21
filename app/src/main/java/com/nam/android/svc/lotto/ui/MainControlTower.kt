package com.nam.android.svc.lotto.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
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
import kotlinx.coroutines.delay
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * @author bs.nam@navercorp.com
 */

@ControlTower
@RequireScreen(MainActivity::class)
@RequireViews(MainViews::class)
class MainControlTower : SVC_MainControlTower(),
    MainViewsAction,
    SelectDialogCreator {

    override var type: SelectMode? = null
    override var count = 0
    override var job: Job? = null

    override val vm by lazy { ViewModelProviders.of(screen).get(MainViewModel::class.java) }

    val selectTypeListener = createDialogListener()

    override fun checkCandidatesCountNotValid(count: Int): Boolean {
        val listSize = vm.candidates.value?.size ?: 0
        return if (listSize < count) {
            showToast(R.string.candidate_count_is_smaller)
            true
        } else {
            false
        }
    }

    override fun onCreated() {
        val candidates = BallPool.createBalls()

        vm.candidates.value = candidates
        vm.selections.value = ArrayList()

        vm.selections.observe(screen, androidx.lifecycle.Observer {
            screen.setReloadMenuItemVisible(it?.size != 0)
        })
    }


    override suspend fun startRemovingSQ(delayTime: Long) {
        for (i in 1..vm.selectCount) {
            delay(delayTime)
            doAsync {
                uiThread {
                    removeRandomOne()
                }
            }
        }
    }

    override fun onClickRandom() {
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
        doAsync {
            val winnerLists = ArrayList<Ball>()
            val candidatelist = vm.candidates.value ?: return@doAsync
            for (i in 0 until vm.selectCount) {
                val indexMax = candidatelist.size
                val random = Random()
                val randomIndex = (random.nextFloat() * indexMax).toInt()
                val removedBall = candidatelist.removeAt(randomIndex)
                winnerLists.add(removedBall)
            }
            uiThread {
                vm.candidates.value = ArrayList(candidatelist)
                vm.selections.value?.addAll(winnerLists)
                vm.selections.value = vm.selections.value
                finishRandom()
            }

        }
    }

    private fun removeRandomOne() {
        val list = vm.candidates.value ?: return
        val indexMax = list.size
        val random = Random()
        val randomIndex = (random.nextFloat() * indexMax).toInt()
        val removedMember = list.removeAt(randomIndex)
        Log.d(TAG, "size=${list.size}, randomIndex=$randomIndex")
        vm.candidates.value = ArrayList(list)
        val winnerList = vm.selections.value ?: ArrayList()
        winnerList.add(removedMember)
        vm.selectedBall.value = removedMember
        vm.selections.value = ArrayList(winnerList)

        count++

        if (count == vm.selectCount) {
            finishRandom()
        }
    }

    private fun finishRandom() {
        showToast("뽑기 완료!")
        type = null
        vm.selectCount = 0
        count = 0
    }

    fun onClickSuffle() {
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