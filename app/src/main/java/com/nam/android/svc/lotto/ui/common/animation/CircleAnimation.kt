package com.nam.android.svc.lotto.ui.common.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils

class CircleAnimation {

    var cancelHide = false
    var isDuringShowAnimation = false
    var isDuringHidingAnimation = false

    fun revealCircle(view: View) {
        if (isDuringShowAnimation) {
            return
        }
        isDuringShowAnimation = true
        cancelHide = true
        val w = view.width
        val h = view.height


        val endRadius = Math.hypot(w.toDouble(), h.toDouble()).toInt()


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            view.visibility = View.VISIBLE
        } else {
            val revealAnimator = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, 0f, endRadius.toFloat())
            view.visibility = View.VISIBLE
            revealAnimator.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(p0: Animator?) {
                    isDuringShowAnimation = false
                }

                override fun onAnimationCancel(p0: Animator?) {
                    isDuringShowAnimation = false
                }

            })
            revealAnimator.duration = 500
            revealAnimator.start()
        }
    }

    fun hideCircle(view: View) {
        if (isDuringHidingAnimation) {
            return
        }
        isDuringHidingAnimation = true
        cancelHide = false
        val w = view.width
        val h = view.height

        val endRadius = Math.hypot(w.toDouble(), h.toDouble()).toInt()


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (!cancelHide) {
                view.visibility = View.INVISIBLE

            }
            return
        } else {
            val anim = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, endRadius.toFloat(), 0f)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    isDuringHidingAnimation = false
                    if (!cancelHide) {
                        view.visibility = View.INVISIBLE
                    }
                }
            })
            anim.duration = 500
            anim.start()
        }
    }
}