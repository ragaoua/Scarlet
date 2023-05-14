package com.example.scarlet

import android.content.res.Resources
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.roundToInt

private const val NO_ACTIVE_BLOCK_MSG = "No active block\nClick to start a new block"
private const val NO_PREVIOUS_BLOCK_MSG = "No previous blocks"

private fun Int.dpToPx(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (this * density).roundToInt()
}

class TrainingLogsActivity : AppCompatActivity() {
    private lateinit var activeBlockBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_logs)

        ////////////////////////
        ///// Active block /////
        ////////////////////////
        activeBlockBtn = findViewById(R.id.activeBlockBtn)
        this.displayActiveBlockSection()

        ////////////////////////
        /// Previous blocks ///
        ////////////////////////
        this.displayPreviousBlocksSection()
    }

    private fun displayActiveBlockSection() {
        val activeBlock = this.getActiveBlock()

        activeBlock?.let {
            TODO("Not yet implemented")
        } ?: run {
            this.activeBlockBtn.text = NO_ACTIVE_BLOCK_MSG
        }
    }

    private fun getActiveBlock(): String? {
        val dbHelper = ScarletDbHelper(this)
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.rawQuery("SELECT name FROM block WHERE NOT completed", null)

        if (cursor.count > 1) {
            TODO("Not Implemented Yet (throw a custom Exception)")
        }

        var activeBlockName: String? = null
        if (cursor.moveToFirst()) {
            activeBlockName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        }

        cursor.close()
        return activeBlockName
    }

    private fun displayPreviousBlocksSection() {
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
        } else {
            TODO("Not yet implemented")
        }
    }

    private fun getPreviousTrainingBlocks(): List<Any> {
        // TODO
        return emptyList()
    }
}