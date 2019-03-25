package com.nam.android.svc.lotto.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.nam.android.svc.lotto.R
import com.nam.android.svc.lotto.ui.dialog.select.SelectMode
import com.nam.android.svc.lotto.ui.dialog.select.SelectTypeDialogListener
import com.nam.android.svc.lotto.vo.Ball
import com.nam.android.svc.lotto.vo.BallPool
import com.naver.android.svc.annotation.ControlTower
import com.naver.android.svc.annotation.RequireScreen
import com.naver.android.svc.annotation.RequireViews
import com.navercorp.android.selective.tools.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * @author bs.nam@navercorp.com
 */

@ControlTower
@RequireScreen(MainActivity::class)
@RequireViews(MainViews::class)
class MainControlTower : SVC_MainControlTower(),
    MainViewsAction {

    var type: SelectMode? = null
    var count = 0
    var removeJob: Job? = null
    var isShuffling: Boolean = false
    var shufflingJob: Job? = null

    val vm by lazy { ViewModelProviders.of(screen).get(MainViewModel::class.java) }

    val selectTypeListener = createDialogListener()

    override fun onCreated() {
        vm.candidates.value = BallPool.createBalls()
        vm.selections.value = ArrayList()

        vm.selections.observe(screen, androidx.lifecycle.Observer {
            screen.setReloadMenuItemVisible(it?.size != 0)
        })
    }


    fun createDialogListener(): SelectTypeDialogListener {
        return object : SelectTypeDialogListener {
            override fun selectJustInTime(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }

                type = SelectMode.JIT
                vm.selectCount = count
                removeJob = launch {
                    removeRandoms()
                }
            }

            override fun selectSequentially(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.SQ
                vm.selectCount = count
                removeJob = launch {
                    startRemovingSQ(800)
                }
            }

            override fun selectWithMyThumb(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.THUMB
                vm.selectCount = count
            }
        }
    }

    fun checkCandidatesCountNotValid(count: Int): Boolean {
        val listSize = vm.candidates.value?.size ?: 0
        return if (listSize < count) {
            showToast(R.string.candidate_count_is_smaller)
            true
        } else {
            false
        }
    }

    fun toggleShuffle() {
        if (isShuffling) {
            isShuffling = false
            shufflingJob?.cancel()
        } else {
            isShuffling = true
            shufflingJob = launch {
                startAutoShuffling(150)
            }
        }
    }

    private suspend fun startAutoShuffling(delayTime: Long) {
        for (i in 1..999) {
            delay(delayTime)
            doAsync {
                uiThread {
                    vm.candidates.value?.shuffle()
                    vm.candidates.value = vm.candidates.value
                }
            }
        }
    }

    override fun onClickSelect() {
        if (type == null) {
            screen.showSelectTypeDialog()
            return
        }

        when (type) {
            SelectMode.JIT -> return
            SelectMode.SQ -> return
            SelectMode.THUMB -> removeRandomOne()
        }
    }

    fun onClickShuffle() {
        toggleShuffle()
    }

    fun onClickCopy() {
        copy()
    }

    fun onClickReload() {
        reload()
    }

    suspend fun startRemovingSQ(delayTime: Long) {
        for (i in 1..vm.selectCount) {
            delay(delayTime)
            doAsync {
                uiThread {
                    removeRandomOne()
                }
            }
        }
    }

    fun removeRandoms() {
        doAsync {
            val winnerLists = ArrayList<Ball>()
            val candidatelist = vm.candidates.value ?: return@doAsync
            for (i in 0 until vm.selectCount) {
                val indexMax = candidatelist.size
                val random = Random()
                val randomIndex = (random.nextFloat() * indexMax).toInt()
                val removedBall = candidatelist.removeAt(randomIndex)
                winnerLists.add(removedBall)
            }
            uiThread {
                vm.candidates.value = ArrayList(candidatelist)
                vm.selections.value?.addAll(winnerLists)
                vm.selections.value = vm.selections.value
                finishRandom()
            }

        }
    }

    fun removeRandomOne() {
        val list = vm.candidates.value ?: return
        val indexMax = list.size
        val random = Random()
        val randomIndex = (random.nextFloat() * indexMax).toInt()
        val removedMember = list.removeAt(randomIndex)
        Log.d(this::class.java.simpleName, "size=${list.size}, randomIndex=$randomIndex")
        vm.candidates.value = ArrayList(list)
        val winnerList = vm.selections.value ?: ArrayList()
        winnerList.add(removedMember)
        vm.selectedBall.value = removedMember
        vm.selections.value = ArrayList(winnerList)

        count++

        if (count == vm.selectCount) {
            finishRandom()
        }
    }

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


    private fun finishRandom() {
        showToast(R.string.finishSelecting)
        type = null
        vm.selectCount = 0
        count = 0
    }

    fun reload() {
        val currentItem = views.getCurrentItem()
        if (currentItem == 0) {
            createNewBalls()
        } else {
            moveSelectionsToCandidates()
        }
    }

    private fun createNewBalls() {
        vm.candidates.value = BallPool.createBalls()
        vm.selections.value = ArrayList()
    }

    private fun moveSelectionsToCandidates() {
        val selections: MutableList<Ball> = vm.selections.value ?: ArrayList()
        val candidates: MutableList<Ball> = vm.candidates.value ?: ArrayList()
        candidates.addAll(selections)
        vm.candidates.value = candidates
        vm.selections.value = ArrayList()
    }

    fun createDialogListener2(): SelectTypeDialogListener {
        return object : SelectTypeDialogListener {
            override fun selectJustInTime(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }

                type = SelectMode.JIT
                vm.selectCount = count
                removeJob = launch {
                    removeRandoms()
                }
            }

            override fun selectSequentially(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.SQ
                vm.selectCount = count
                removeJob = launch {
                    startRemovingSQ(800)
                }
            }

            override fun selectWithMyThumb(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.THUMB
                vm.selectCount = count
            }
        }
    }

    fun checkCandidatesCountNotValid2(count: Int): Boolean {
        val listSize = vm.candidates.value?.size ?: 0
        return if (listSize < count) {
            showToast(R.string.candidate_count_is_smaller)
            true
        } else {
            false
        }
    }

    fun toggleShuffle2() {
        if (isShuffling) {
            isShuffling = false
            shufflingJob?.cancel()
        } else {
            isShuffling = true
            shufflingJob = launch {
                startAutoShuffling(150)
            }
        }
    }

    suspend fun startAutoShuffling2(delayTime: Long) {
        for (i in 1..999) {
            delay(delayTime)
            doAsync {
                uiThread {
                    vm.candidates.value?.shuffle()
                    vm.candidates.value = vm.candidates.value
                }
            }
        }
    }

    fun onClickSelect2() {
        if (type == null) {
            screen.showSelectTypeDialog()
            return
        }

        when (type) {
            SelectMode.JIT -> return
            SelectMode.SQ -> return
            SelectMode.THUMB -> removeRandomOne()
        }
    }

    suspend fun startRemovingSQ2(delayTime: Long) {
        for (i in 1..vm.selectCount) {
            delay(delayTime)
            doAsync {
                uiThread {
                    removeRandomOne()
                }
            }
        }
    }

    fun removeRandoms2() {
        doAsync {
            val winnerLists = ArrayList<Ball>()
            val candidatelist = vm.candidates.value ?: return@doAsync
            for (i in 0 until vm.selectCount) {
                val indexMax = candidatelist.size
                val random = Random()
                val randomIndex = (random.nextFloat() * indexMax).toInt()
                val removedBall = candidatelist.removeAt(randomIndex)
                winnerLists.add(removedBall)
            }
            uiThread {
                vm.candidates.value = ArrayList(candidatelist)
                vm.selections.value?.addAll(winnerLists)
                vm.selections.value = vm.selections.value
                finishRandom()
            }

        }
    }

    fun removeRandomOne2() {
        val list = vm.candidates.value ?: return
        val indexMax = list.size
        val random = Random()
        val randomIndex = (random.nextFloat() * indexMax).toInt()
        val removedMember = list.removeAt(randomIndex)
        Log.d(this::class.java.simpleName, "size=${list.size}, randomIndex=$randomIndex")
        vm.candidates.value = ArrayList(list)
        val winnerList = vm.selections.value ?: ArrayList()
        winnerList.add(removedMember)
        vm.selectedBall.value = removedMember
        vm.selections.value = ArrayList(winnerList)

        count++

        if (count == vm.selectCount) {
            finishRandom()
        }
    }

    fun copy2() {
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


    private fun finishRandom2() {
        showToast(R.string.finishSelecting)
        type = null
        vm.selectCount = 0
        count = 0
    }

    fun reload2() {
        val currentItem = views.getCurrentItem()
        if (currentItem == 0) {
            createNewBalls()
        } else {
            moveSelectionsToCandidates()
        }
    }

    private fun createNewBalls2() {
        vm.candidates.value = BallPool.createBalls()
        vm.selections.value = ArrayList()
    }

    private fun moveSelectionsToCandidates2() {
        val selections: MutableList<Ball> = vm.selections.value ?: ArrayList()
        val candidates: MutableList<Ball> = vm.candidates.value ?: ArrayList()
        candidates.addAll(selections)
        vm.candidates.value = candidates
        vm.selections.value = ArrayList()
    }


    fun createDialogListener3(): SelectTypeDialogListener {
        return object : SelectTypeDialogListener {
            override fun selectJustInTime(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }

                type = SelectMode.JIT
                vm.selectCount = count
                removeJob = launch {
                    removeRandoms()
                }
            }

            override fun selectSequentially(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.SQ
                vm.selectCount = count
                removeJob = launch {
                    startRemovingSQ(800)
                }
            }

            override fun selectWithMyThumb(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.THUMB
                vm.selectCount = count
            }
        }
    }

    fun checkCandidatesCountNotValid3(count: Int): Boolean {
        val listSize = vm.candidates.value?.size ?: 0
        return if (listSize < count) {
            showToast(R.string.candidate_count_is_smaller)
            true
        } else {
            false
        }
    }

    fun toggleShuffle3() {
        if (isShuffling) {
            isShuffling = false
            shufflingJob?.cancel()
        } else {
            isShuffling = true
            shufflingJob = launch {
                startAutoShuffling(150)
            }
        }
    }

    suspend fun startAutoShuffling3(delayTime: Long) {
        for (i in 1..999) {
            delay(delayTime)
            doAsync {
                uiThread {
                    vm.candidates.value?.shuffle()
                    vm.candidates.value = vm.candidates.value
                }
            }
        }
    }

    fun onClickSelect3() {
        if (type == null) {
            screen.showSelectTypeDialog()
            return
        }

        when (type) {
            SelectMode.JIT -> return
            SelectMode.SQ -> return
            SelectMode.THUMB -> removeRandomOne()
        }
    }

    suspend fun startRemovingSQ3(delayTime: Long) {
        for (i in 1..vm.selectCount) {
            delay(delayTime)
            doAsync {
                uiThread {
                    removeRandomOne()
                }
            }
        }
    }

    fun removeRandoms3() {
        doAsync {
            val winnerLists = ArrayList<Ball>()
            val candidatelist = vm.candidates.value ?: return@doAsync
            for (i in 0 until vm.selectCount) {
                val indexMax = candidatelist.size
                val random = Random()
                val randomIndex = (random.nextFloat() * indexMax).toInt()
                val removedBall = candidatelist.removeAt(randomIndex)
                winnerLists.add(removedBall)
            }
            uiThread {
                vm.candidates.value = ArrayList(candidatelist)
                vm.selections.value?.addAll(winnerLists)
                vm.selections.value = vm.selections.value
                finishRandom()
            }

        }
    }

    fun removeRandomOne3() {
        val list = vm.candidates.value ?: return
        val indexMax = list.size
        val random = Random()
        val randomIndex = (random.nextFloat() * indexMax).toInt()
        val removedMember = list.removeAt(randomIndex)
        Log.d(this::class.java.simpleName, "size=${list.size}, randomIndex=$randomIndex")
        vm.candidates.value = ArrayList(list)
        val winnerList = vm.selections.value ?: ArrayList()
        winnerList.add(removedMember)
        vm.selectedBall.value = removedMember
        vm.selections.value = ArrayList(winnerList)

        count++

        if (count == vm.selectCount) {
            finishRandom()
        }
    }

    fun copy3() {
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


    private fun finishRandom3() {
        showToast(R.string.finishSelecting)
        type = null
        vm.selectCount = 0
        count = 0
    }

    fun reload3() {
        val currentItem = views.getCurrentItem()
        if (currentItem == 0) {
            createNewBalls()
        } else {
            moveSelectionsToCandidates()
        }
    }

    private fun createNewBalls3() {
        vm.candidates.value = BallPool.createBalls()
        vm.selections.value = ArrayList()
    }

    private fun moveSelectionsToCandidates3() {
        val selections: MutableList<Ball> = vm.selections.value ?: ArrayList()
        val candidates: MutableList<Ball> = vm.candidates.value ?: ArrayList()
        candidates.addAll(selections)
        vm.candidates.value = candidates
        vm.selections.value = ArrayList()
    }



    fun createDialogListener4(): SelectTypeDialogListener {
        return object : SelectTypeDialogListener {
            override fun selectJustInTime(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }

                type = SelectMode.JIT
                vm.selectCount = count
                removeJob = launch {
                    removeRandoms()
                }
            }

            override fun selectSequentially(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.SQ
                vm.selectCount = count
                removeJob = launch {
                    startRemovingSQ(800)
                }
            }

            override fun selectWithMyThumb(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.THUMB
                vm.selectCount = count
            }
        }
    }

    fun checkCandidatesCountNotValid4(count: Int): Boolean {
        val listSize = vm.candidates.value?.size ?: 0
        return if (listSize < count) {
            showToast(R.string.candidate_count_is_smaller)
            true
        } else {
            false
        }
    }

    fun toggleShuffle4() {
        if (isShuffling) {
            isShuffling = false
            shufflingJob?.cancel()
        } else {
            isShuffling = true
            shufflingJob = launch {
                startAutoShuffling(150)
            }
        }
    }

    suspend fun startAutoShuffling4(delayTime: Long) {
        for (i in 1..999) {
            delay(delayTime)
            doAsync {
                uiThread {
                    vm.candidates.value?.shuffle()
                    vm.candidates.value = vm.candidates.value
                }
            }
        }
    }

    fun onClickSelect4() {
        if (type == null) {
            screen.showSelectTypeDialog()
            return
        }

        when (type) {
            SelectMode.JIT -> return
            SelectMode.SQ -> return
            SelectMode.THUMB -> removeRandomOne()
        }
    }

    suspend fun startRemovingSQ4(delayTime: Long) {
        for (i in 1..vm.selectCount) {
            delay(delayTime)
            doAsync {
                uiThread {
                    removeRandomOne()
                }
            }
        }
    }

    fun removeRandoms4() {
        doAsync {
            val winnerLists = ArrayList<Ball>()
            val candidatelist = vm.candidates.value ?: return@doAsync
            for (i in 0 until vm.selectCount) {
                val indexMax = candidatelist.size
                val random = Random()
                val randomIndex = (random.nextFloat() * indexMax).toInt()
                val removedBall = candidatelist.removeAt(randomIndex)
                winnerLists.add(removedBall)
            }
            uiThread {
                vm.candidates.value = ArrayList(candidatelist)
                vm.selections.value?.addAll(winnerLists)
                vm.selections.value = vm.selections.value
                finishRandom()
            }

        }
    }

    fun removeRandomOne4() {
        val list = vm.candidates.value ?: return
        val indexMax = list.size
        val random = Random()
        val randomIndex = (random.nextFloat() * indexMax).toInt()
        val removedMember = list.removeAt(randomIndex)
        Log.d(this::class.java.simpleName, "size=${list.size}, randomIndex=$randomIndex")
        vm.candidates.value = ArrayList(list)
        val winnerList = vm.selections.value ?: ArrayList()
        winnerList.add(removedMember)
        vm.selectedBall.value = removedMember
        vm.selections.value = ArrayList(winnerList)

        count++

        if (count == vm.selectCount) {
            finishRandom()
        }
    }

    fun copy4() {
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


    private fun finishRandom4() {
        showToast(R.string.finishSelecting)
        type = null
        vm.selectCount = 0
        count = 0
    }

    fun reload4() {
        val currentItem = views.getCurrentItem()
        if (currentItem == 0) {
            createNewBalls()
        } else {
            moveSelectionsToCandidates()
        }
    }

    private fun createNewBalls4() {
        vm.candidates.value = BallPool.createBalls()
        vm.selections.value = ArrayList()
    }

    private fun moveSelectionsToCandidates4() {
        val selections: MutableList<Ball> = vm.selections.value ?: ArrayList()
        val candidates: MutableList<Ball> = vm.candidates.value ?: ArrayList()
        candidates.addAll(selections)
        vm.candidates.value = candidates
        vm.selections.value = ArrayList()
    }


    fun createDialogListener5(): SelectTypeDialogListener {
        return object : SelectTypeDialogListener {
            override fun selectJustInTime(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }

                type = SelectMode.JIT
                vm.selectCount = count
                removeJob = launch {
                    removeRandoms()
                }
            }

            override fun selectSequentially(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.SQ
                vm.selectCount = count
                removeJob = launch {
                    startRemovingSQ(800)
                }
            }

            override fun selectWithMyThumb(count: Int) {
                if (checkCandidatesCountNotValid(count)) {
                    return
                }
                type = SelectMode.THUMB
                vm.selectCount = count
            }
        }
    }

    fun checkCandidatesCountNotValid5(count: Int): Boolean {
        val listSize = vm.candidates.value?.size ?: 0
        return if (listSize < count) {
            showToast(R.string.candidate_count_is_smaller)
            true
        } else {
            false
        }
    }

    fun toggleShuffle5() {
        if (isShuffling) {
            isShuffling = false
            shufflingJob?.cancel()
        } else {
            isShuffling = true
            shufflingJob = launch {
                startAutoShuffling(150)
            }
        }
    }

    suspend fun startAutoShuffling5(delayTime: Long) {
        for (i in 1..999) {
            delay(delayTime)
            doAsync {
                uiThread {
                    vm.candidates.value?.shuffle()
                    vm.candidates.value = vm.candidates.value
                }
            }
        }
    }

    fun onClickSelect5() {
        if (type == null) {
            screen.showSelectTypeDialog()
            return
        }

        when (type) {
            SelectMode.JIT -> return
            SelectMode.SQ -> return
            SelectMode.THUMB -> removeRandomOne()
        }
    }

    suspend fun startRemovingSQ5(delayTime: Long) {
        for (i in 1..vm.selectCount) {
            delay(delayTime)
            doAsync {
                uiThread {
                    removeRandomOne()
                }
            }
        }
    }

    fun removeRandoms5() {
        doAsync {
            val winnerLists = ArrayList<Ball>()
            val candidatelist = vm.candidates.value ?: return@doAsync
            for (i in 0 until vm.selectCount) {
                val indexMax = candidatelist.size
                val random = Random()
                val randomIndex = (random.nextFloat() * indexMax).toInt()
                val removedBall = candidatelist.removeAt(randomIndex)
                winnerLists.add(removedBall)
            }
            uiThread {
                vm.candidates.value = ArrayList(candidatelist)
                vm.selections.value?.addAll(winnerLists)
                vm.selections.value = vm.selections.value
                finishRandom()
            }

        }
    }

    fun removeRandomOne5() {
        val list = vm.candidates.value ?: return
        val indexMax = list.size
        val random = Random()
        val randomIndex = (random.nextFloat() * indexMax).toInt()
        val removedMember = list.removeAt(randomIndex)
        Log.d(this::class.java.simpleName, "size=${list.size}, randomIndex=$randomIndex")
        vm.candidates.value = ArrayList(list)
        val winnerList = vm.selections.value ?: ArrayList()
        winnerList.add(removedMember)
        vm.selectedBall.value = removedMember
        vm.selections.value = ArrayList(winnerList)

        count++

        if (count == vm.selectCount) {
            finishRandom()
        }
    }

    fun copy5() {
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


    private fun finishRandom5() {
        showToast(R.string.finishSelecting)
        type = null
        vm.selectCount = 0
        count = 0
    }

    fun reload5() {
        val currentItem = views.getCurrentItem()
        if (currentItem == 0) {
            createNewBalls()
        } else {
            moveSelectionsToCandidates()
        }
    }

    private fun createNewBalls5() {
        vm.candidates.value = BallPool.createBalls()
        vm.selections.value = ArrayList()
    }

    private fun moveSelectionsToCandidates5() {
        val selections: MutableList<Ball> = vm.selections.value ?: ArrayList()
        val candidates: MutableList<Ball> = vm.candidates.value ?: ArrayList()
        candidates.addAll(selections)
        vm.candidates.value = candidates
        vm.selections.value = ArrayList()
    }

}