package com.nam.android.svc.lotto.ui.tabs.selections

import androidx.lifecycle.MutableLiveData
import com.nam.android.svc.lotto.ui.MainViewModel
import com.nam.android.svc.lotto.ui.tabs.AbsBallListViews
import com.nam.android.svc.lotto.vo.Ball


/**
 * @author bs.nam@navercorp.com
 */
class CandidatesViews(vm: MainViewModel) : AbsBallListViews(vm) {
    override val ballList: MutableLiveData<MutableList<Ball>>
        get() = vm.candidates
}