package com.nam.android.svc.lotto.ui.controller

import com.nam.android.svc.lotto.ui.MainViewModel
import com.navercorp.android.selective.tools.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * @author bs.nam@navercorp.com
 */
interface ShuffleController {

    val vm: MainViewModel
    var isShuffling :Boolean
    var shufflingJob: Job?

    fun toggleShuffle() {
        if(isShuffling){
            isShuffling = false
            shufflingJob?.cancel()
        }else{
            isShuffling = true
            shufflingJob = launch {
                startAutoShuffling(150)
            }
        }
    }

    suspend fun startAutoShuffling(delayTime: Long) {
        for (i in 1..999) {
            delay(delayTime)
            doAsync {
                uiThread {
                    vm.candidates.value?.shuffle()
                    vm.candidates.value = vm.candidates.value
                }
            }
        }
    }
}