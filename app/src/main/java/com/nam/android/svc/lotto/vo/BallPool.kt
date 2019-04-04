package com.nam.android.svc.lotto.vo

import android.graphics.Color

/**
 * @author bs.nam@navercorp.com
 */
object BallPool {
    
    fun createBalls(): MutableList<Ball> {
        val list = ArrayList<Ball>()
        for (i in 1..45) {
            list.add(Ball(i, getRandomColor()))
        }
        return list
    }

    private fun getRandomColor(): Int {
        return Color.rgb(
            Math.floor(Math.random() * 128).toInt() + 64,
            Math.floor(Math.random() * 128).toInt() + 64,
            Math.floor(Math.random() * 128).toInt() + 64
        )
    }
}