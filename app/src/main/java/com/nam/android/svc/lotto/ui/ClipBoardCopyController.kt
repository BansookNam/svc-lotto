package com.nam.android.svc.lotto.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.nam.android.svc.lotto.R

/**
 * @author bs.nam@navercorp.com
 */
interface ClipBoardCopyController {
    val vm: MainViewModel
    val context: Context?
    val views: MainViews

    fun copy() {
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

    fun showToast(resId: Int)
}