package com.nam.android.svc.lotto.ui.tabs

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.nam.android.svc.lotto.R
import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.recyclerview.ActionAdapter


class BallAdapter(action: BallViewsAction) : ActionAdapter<BallViewHolder, BallViewsAction>(action) {
    var list: MutableList<Ball> = ArrayList()
    private val differ = AsyncListDiffer(this, BallDiffUtilCallback())

    override fun onBindHolder(holder: BallViewHolder, adapterPosition: Int) {
        val member = list[adapterPosition]
        holder.bindView(member, adapterPosition)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BallViewHolder {
        return BallViewHolder(viewType, parent, action)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_ball
    }

    fun updateList(newList: MutableList<Ball>) {
        list.clear()
        list.addAll(newList)
        differ.submitList(newList)
    }

}