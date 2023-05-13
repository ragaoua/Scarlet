package com.example.scarlet

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.roundToInt

private val NO_ACTIVE_BLOCK_MSG = "No active block\nClick to start a new block"
private val NO_PREVIOUS_BLOCK_MSG = "No previous blocks"

private fun Int.dpToPx(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (this * density).roundToInt()
}

private fun getActiveBlock(): Any? {
    // TODO
    return null
}

private fun getPreviousTrainingBlocks(): List<Any> {
    // TODO
    return emptyList<Any>()
}

class TrainingLogsActivity : AppCompatActivity() {
    private lateinit var activeBlockBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_logs)

        activeBlockBtn = findViewById(R.id.activeBlockBtn)

        ////////////////////////
        ///// Active block /////
        ////////////////////////
        val activeBlock = getActiveBlock()
        activeBlock?.let {
            TODO("Not yet implemented")
        } ?: run {
            activeBlockBtn.text = NO_ACTIVE_BLOCK_MSG
        }

        ////////////////////////
        /// Previous blocks ///
        ////////////////////////
        val previousBlocks = getPreviousTrainingBlocks()
        if (previousBlocks.isEmpty()) {
            val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
            val noPreviousBlocksTv = TextView(this)
            noPreviousBlocksTv.id = View.generateViewId()
            noPreviousBlocksTv.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToBottom = R.id.previousBlocksTv
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topMargin = 32.dpToPx()
            }
            noPreviousBlocksTv.text = NO_PREVIOUS_BLOCK_MSG

            constraintLayout.addView(noPreviousBlocksTv)

        }
        else {
            TODO("Not yet implemented")
        }
    }
}