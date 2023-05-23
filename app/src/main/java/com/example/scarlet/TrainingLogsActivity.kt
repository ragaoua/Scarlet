package com.example.scarlet

import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
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
    private lateinit var createBlockBtn: Button
    private lateinit var newBlockNameEt: EditText

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
                inflateNewBlockPopupView()
            }
        }
    }

    private fun inflateNewBlockPopupView() {
        val popupView = layoutInflater.inflate(R.layout.activity_add_block_popup, null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        createBlockBtn = popupView.findViewById(R.id.createBlockBtn)
        newBlockNameEt = popupView.findViewById(R.id.blockNameEt)

        definePopupViewListeners()
    }

    private fun definePopupViewListeners() {
        createBlockBtn.setOnClickListener{
            val blockName = newBlockNameEt.text.toString().trim()

            if (blockName.isEmpty()){
                TODO("Print a message indicating that the block name is empty")
            }
            else {
                val blockId = createBlock(blockName)

                val intent = Intent(this, ShowBlockActivity::class.java)
                intent.putExtra("blockId", blockId)
                startActivity(intent)
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

    private fun createBlock(blockName: String): Long {
        val dbHelper = ScarletDbHelper(this)
        val db = dbHelper.writableDatabase

        val statement = db.compileStatement("INSERT INTO block(name) VALUES(?)")
        statement.bindString(1, blockName)

        val blockId: Long = statement.executeInsert()
        statement.close()

        return blockId
    }
}