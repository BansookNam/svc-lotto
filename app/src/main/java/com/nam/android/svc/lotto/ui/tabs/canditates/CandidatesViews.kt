package com.nam.android.svc.lotto.ui.tabs.canditates

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.ui.MainViewModel
import com.nam.android.svc.lotto.ui.tabs.AbsBallListViews
import com.nam.android.svc.lotto.vo.Ball


/**
 * @author bs.nam@navercorp.com
 */
class CandidatesViews : AbsBallListViews() {

    private val vm by lazy {
        ViewModelProviders
            .of(screen.hostActivity!!)
            .get(MainViewModel::class.java)
    }

    override val ballList: MutableLiveData<MutableList<Ball>>
        get() = vm.candidates
}

