package com.example.scarlet

import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.roundToInt

private const val NO_PREVIOUS_BLOCK_MSG = "No previous blocks"

private fun dpToPx(dp: Int): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (dp * density).roundToInt()
}

class TrainingLogsActivity : AppCompatActivity() {
    private lateinit var activeBlockBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_logs)
        activeBlockBtn = findViewById(R.id.activeBlockBtn)

        this.displayActiveBlockSection()

        this.displayPreviousBlocksSection()
    }

    private fun displayActiveBlockSection() {
        val activeBlock = this.getActiveBlockName()

        activeBlock?.let {
            TODO("Not yet implemented")
        } ?: run {
            this.activeBlockBtn.setOnClickListener {
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val popupView = inflater.inflate(R.layout.popup_add_block, null)

                // create the popup window
                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val focusable = true // lets taps outside the popup also dismiss it
                val popupWindow = PopupWindow(popupView, width, height, focusable)

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window token
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
            }
        }
    }

    private fun getActiveBlockName(): String? {
        val dbHelper = ScarletDbHelper(this)
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.rawQuery("SELECT name FROM block WHERE NOT completed", null)

        if (cursor.count > 1) {
            TODO("Throw a custom Exception")
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
            val noPreviousBlocksTv = TextView(this)
            noPreviousBlocksTv.id = View.generateViewId()
            noPreviousBlocksTv.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToBottom = R.id.previousBlocksTv
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topMargin = dpToPx(32)
            }
            noPreviousBlocksTv.text = NO_PREVIOUS_BLOCK_MSG

            val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
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