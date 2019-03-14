package com.nam.android.svc.lotto.ui

import android.view.Menu
import android.view.MenuItem
import com.nam.android.svc.lotto.R
import com.naver.android.svc.core.screen.SvcActivity

class MainActivity : SvcActivity<MainViews, MainControlTower>() {
    override fun createControlTower() = MainControlTower(this, views)
    override fun createViews() = MainViews()

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
            R.id.action_shuffle -> controlTower.onClickSuffle()
            R.id.action_copy -> controlTower.onClickCopy()
            R.id.action_reload -> controlTower.onClickReload()
        }
        return true
    }
}
