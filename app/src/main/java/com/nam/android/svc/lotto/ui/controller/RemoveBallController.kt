package com.nam.android.svc.lotto.ui.controller

import android.util.Log
import com.nam.android.svc.lotto.R
import com.nam.android.svc.lotto.ui.MainViewModel
import com.nam.android.svc.lotto.ui.dialog.select.SelectMode
import com.nam.android.svc.lotto.vo.Ball
import kotlinx.coroutines.delay
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * @author bs.nam@navercorp.com
 */
interface RemoveBallController {

    var type: SelectMode?
    var count: Int
    val vm: MainViewModel

    suspend fun startRemovingSQ(delayTime: Long) {
        for (i in 1..vm.selectCount) {
            delay(delayTime)
            doAsync {
                uiThread {
                    removeRandomOne()
                }
            }
        }
    }

    fun removeRandoms() {
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

    fun removeRandomOne() {
        val list = vm.candidates.value ?: return
        val indexMax = list.size
        val random = Random()
        val randomIndex = (random.nextFloat() * indexMax).toInt()
        val removedMember = list.removeAt(randomIndex)
        Log.d(this::class.java.simpleName, "size=${list.size}, randomIndex=$randomIndex")
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

    fun finishRandom() {
        showToast(R.string.finishSelecting)
        type = null
        vm.selectCount = 0
        count = 0
    }

    fun showToast(resId: Int)

}