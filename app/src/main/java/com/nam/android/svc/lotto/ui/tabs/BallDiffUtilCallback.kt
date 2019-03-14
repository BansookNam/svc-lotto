package com.nam.android.svc.lotto.ui.tabs

import androidx.recyclerview.widget.DiffUtil
import com.nam.android.svc.lotto.vo.Ball

class BallDiffUtilCallback() : DiffUtil.ItemCallback<Ball>() {
    override fun areItemsTheSame(oldItem: Ball, newItem: Ball): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Ball, newItem: Ball): Boolean {
        return oldItem == newItem
    }


}