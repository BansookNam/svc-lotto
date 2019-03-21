package com.nam.android.svc.lotto.ui

import android.graphics.PorterDuff
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.view.MenuItem
import android.view.MotionEvent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.nam.android.svc.lotto.R
import com.nam.android.svc.lotto.ui.common.animation.CircleAnimation
import com.nam.android.svc.lotto.ui.controller.IndexProvideViews
import com.nam.android.svc.lotto.vo.Ball
import com.naver.android.svc.core.views.ActionViews
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * @author bs.nam@navercorp.com
 */
class MainViews : ActionViews<MainViewsAction>(), IndexProvideViews {
    override val layoutResId: Int get() =  R.layout.activity_main

    private val vm by lazy { ViewModelProviders.of(screen.hostActivity!!).get(MainViewModel::class.java) }

    private val adapter: MainPagerAdapter by lazy { MainPagerAdapter(screen.screenFragmentManager!!) }
    private val circleAnimation = CircleAnimation()
    private val menuItemsHolder by lazy {
        MenuHolder(
            getNavigationMenuItem(R.id.navigation_candidate),
            getNavigationMenuItem(R.id.navigation_winner)
        )
    }

    fun getNavigationMenuItem(id: Int): MenuItem {
        return rootView.navigation.menu.findItem(id)
    }

    override fun onCreated() {
        withRootView {
            tv_selected_ball.background = ShapeDrawable(OvalShape())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv_selected_ball.clipToOutline = true
            }
            setNavi()
            viewPager.adapter = adapter
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    navigation.selectedItemId = if (position == 0) R.id.navigation_candidate else R.id.navigation_winner
                }

            })
            viewPager.setOnTouchListener { _, motionEvent ->

                if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                    clearSelectedBall()
                    return@setOnTouchListener false
                }

                return@setOnTouchListener false

            }
        }
        observeSelectedCandidate()
        observeCount()
    }

    private fun clearSelectedBall() {
        vm.selectedBall.value = null
    }

    private fun observeCount() {

        vm.selections.observe(screen, Observer {
            withRootView {
                it ?: return@Observer
                menuItemsHolder.menuSelected.title = getString(R.string.selected_count, it.size)
            }
        })

        vm.candidates.observe(screen, Observer {
            withRootView {
                it ?: return@Observer
                menuItemsHolder.menuCandidate.title = getString(R.string.candidate_count, it.size)
            }
        })
    }

    private fun observeSelectedCandidate() {
        vm.selectedBall.observe(screen, Observer<Ball> {
            if (!rootView.selected_ball_container.isAttachedToWindow) {
                return@Observer
            }

            if (it == null) {
                circleAnimation.hideCircle(rootView.selected_ball_container)
                return@Observer
            }

            withRootView {
                tv_selected_ball.text = it.number.toString()
                tv_selected_ball.background = ShapeDrawable(OvalShape()).apply {
                    setColorFilter(it.color, PorterDuff.Mode.SRC_IN)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tv_selected_ball.clipToOutline = true
                }
            }

            circleAnimation.revealCircle(rootView.selected_ball_container)
            postDelayed(Runnable {
                circleAnimation.hideCircle(rootView.selected_ball_container)
            }, 500)
        })
    }

    private val returnToSelectedTab = Runnable {
        val selectedCurrentTab = rootView.viewPager.currentItem
        rootView.navigation.selectedItemId =
            if (selectedCurrentTab == 0) R.id.navigation_candidate else R.id.navigation_winner
    }

    private fun setNavi() {
        withRootView {
            navigation.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_select -> {
                        viewsAction.onClickSelect()
                        removeCallbacks(returnToSelectedTab)
                        postDelayed(returnToSelectedTab, 700)
                    }

                    R.id.navigation_candidate -> {
                        viewPager.currentItem = 0
                    }
                    R.id.navigation_winner -> {
                        viewPager.currentItem = 1
                    }
                }
                true
            }
        }
    }

    override fun getCurrentItem(): Int {
        return rootView.viewPager.currentItem
    }
}