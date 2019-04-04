package com.nam.android.svc.lotto.ui.tabs.selections

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.ui.MainViewModel
import com.nam.android.svc.lotto.ui.tabs.AbsBallListViews
import com.nam.android.svc.lotto.vo.Ball


/**
 * @author bs.nam@navercorp.com
 */
class SelectionsViews : AbsBallListViews() {

    private val vm by lazy {
        ViewModelProviders
            .of(screen.hostActivity!!)
            .get(MainViewModel::class.java)
    }

    override val ballList: MutableLiveData<MutableList<Ball>>
        get() = vm.selections
}

