package com.nam.android.svc.lotto.ui

import android.view.Menu
import android.view.MenuItem
import com.nam.android.svc.lotto.R
import com.naver.android.annotation.RequireControlTower
import com.naver.android.annotation.RequireViews
import com.naver.android.annotation.SvcActivity
import com.naver.android.svc.svcpeoplelotto.ui.dialog.select.SelectTypeDialog


@SvcActivity
@RequireViews(MainViews::class)
@RequireControlTower(MainControlTower::class)
class MainActivity : SVC_MainActivity() {

    private var reloadMenuItem: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        reloadMenuItem = menu.findItem(R.id.action_reload)
        return true
    }

    fun setReloadMenuItemVisible(isVisible: Boolean) {
        reloadMenuItem?.isVisible = isVisible
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_shuffle -> controlTower.onClickShuffle()
            R.id.action_copy -> controlTower.onClickCopy()
            R.id.action_reload -> controlTower.onClickReload()
        }
        return true
    }

    fun showSelectTypeDialog() {
        val dialog = SelectTypeDialog()
        dialog.dialogListener = controlTower.selectTypeListener
        showDialog(dialog)
    }
}
