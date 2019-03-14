package com.nam.android.svc.lotto.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nam.android.svc.lotto.vo.Ball

class MainViewModel: ViewModel(){
    val candidates = MutableLiveData<MutableList<Ball>>()
    val selections = MutableLiveData<MutableList<Ball>>()
    val selectedBall = MutableLiveData<Ball>()
    var selectCount = 0
}