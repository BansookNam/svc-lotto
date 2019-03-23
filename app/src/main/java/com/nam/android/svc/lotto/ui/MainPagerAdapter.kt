package com.nam.android.svc.lotto.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nam.android.svc.lotto.ui.tabs.canditates.CandidatesFragment
import com.nam.android.svc.lotto.ui.tabs.selections.SelectionsFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val candidates = CandidatesFragment()
    private val selections = SelectionsFragment()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> candidates
            else -> selections
        }
    }

    override fun getCount() = 2
}