package com.example.scarlet

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.roundToInt

private const val TAG = "TrainingLogsActivity"
private const val NO_COMPLETED_BLOCK_MSG = "No completed block"

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

        displayActiveBlockSection()

        displayCompletedBlocksSection()
    }

    /**
     * Displays the "Active block" section of the activity.
     *
     * If an active block is found, sets the listener for activeBlockBtn
     * to open the [ShowBlockActivity] for that block. Otherwise, the button
     * inflates a popup to create a new block.
     */
    private fun displayActiveBlockSection() {
        val activeBlock = this.getActiveBlock()

        activeBlock?.let {
            this.activeBlockBtn.text = activeBlock.name
            this.activeBlockBtn.setOnClickListener {
                val intent = Intent(this, ShowBlockActivity::class.java)
                intent.putExtra("block", activeBlock)
                startActivity(intent)
            }
        } ?: run {
            this.activeBlockBtn.setOnClickListener {
                inflateNewBlockPopupView()
            }
        }
    }

    /**
     * Inflates a popup to create a new block (activity_add_block_popup)
     */
    private fun inflateNewBlockPopupView() {
        val popupView = layoutInflater.inflate(R.layout.activity_add_block_popup, null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        createBlockBtn = popupView.findViewById(R.id.createBlockBtn)
        newBlockNameEt = popupView.findViewById(R.id.blockNameEt)

        defineNewBlockPopupViewListeners()
    }

    /**
     * Defines on click listener for [createBlockBtn].
     *
     * The button create a block and opens the [ShowBlockActivity] for that block
     */
    private fun defineNewBlockPopupViewListeners() {
        createBlockBtn.setOnClickListener{
            val blockName = newBlockNameEt.text.toString().trim()

            if (blockName.isEmpty()){
                // TODO
                Log.d(TAG, "Print a message indicating that the block name is empty")
            }
            else {
                val block = createBlock(blockName)

                val intent = Intent(this, ShowBlockActivity::class.java)
                intent.putExtra("block", block)
                startActivity(intent)
            }
        }
    }

    /**
     * Queries the database for the active block, then returns it.
     *
     * Throws a Exception if multiple active blocks are found.
     *
     * @return activeBlock
     */
    private fun getActiveBlock(): Block? {
        val dbHelper = ScarletDbHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM block WHERE NOT completed", null)

        if (cursor.count > 1) {
            throw Exception("Too many active blocks. Should only get one")
        }

        var activeBlock: Block? = null
        if (cursor.moveToFirst()) {
            activeBlock = Block(cursor)
        }

        cursor.close()
        db.close()

        return activeBlock
    }

    /**
     * Displays the "Previous blocks" section of the activity.
     *
     * Gets the previous training blocks, and displays a button for each.
     *
     * If no previous blocks are found, displays a TextView indicating so
     */
    private fun displayCompletedBlocksSection() {
        val previousBlocks = getCompletedTrainingBlocks()

        if (previousBlocks.isEmpty()) {
            val noCompletedBlocksTv = TextView(this)
            noCompletedBlocksTv.id = View.generateViewId()
            noCompletedBlocksTv.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToBottom = R.id.previousBlocksTv
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topMargin = dpToPx(32)
            }
            noCompletedBlocksTv.text = NO_COMPLETED_BLOCK_MSG

            val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
            constraintLayout.addView(noCompletedBlocksTv)
        } else {
            // TODO
            Log.d(TAG, "Display a button for each block")
        }
    }

    /**
     * @return List of completed blocks
     */
    private fun getCompletedTrainingBlocks(): List<Block> {
        // TODO
        return emptyList()
    }

    /**
     * Creates a training block
     *
     * @param blockName Name of the block to create
     *
     * @return The created block
     */
    private fun createBlock(blockName: String): Block {
        val dbHelper = ScarletDbHelper(this)
        val db = dbHelper.writableDatabase

        val statement = db.compileStatement("INSERT INTO block(name) VALUES(?)")
        statement.bindString(1, blockName)

        val block = Block(statement.executeInsert(), blockName, false)

        statement.close()
        db.close()
        return block
    }
}