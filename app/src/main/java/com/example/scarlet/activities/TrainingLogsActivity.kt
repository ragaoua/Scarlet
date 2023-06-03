package com.example.scarlet.activities

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.scarlet.model.Block
import com.example.scarlet.R
import com.example.scarlet.db.ScarletDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val TAG = "TrainingLogsActivity"
private const val NO_COMPLETED_BLOCK_MSG = "No completed blocks"

private fun dpToPx(dp: Int): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (dp * density).roundToInt()
}

class TrainingLogsActivity : AppCompatActivity() {
    private lateinit var activeBlockBtn: Button
    private lateinit var createBlockBtn: Button
    private lateinit var newBlockNameEt: EditText
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_logs)
        activeBlockBtn = findViewById(R.id.activeBlockBtn)
        constraintLayout = findViewById(R.id.constraintLayout)

        displayActiveBlockSection()
        displayCompletedBlocksSection()
    }

    /**
     * Displays the "Active block" section of the activity.
     *
     * If an active block is found, sets the listener for activeBlockBtn
     * to open the [BlockActivity] for that block. Otherwise, the button
     * inflates a popup to create a new block.
     */
    private fun displayActiveBlockSection() {
        val dbInstance = ScarletDatabase.getInstance(this)

        lifecycleScope.launch(Dispatchers.IO) {
            val uncompletedBlocks = dbInstance.blockDao().getBlocksByCompleted(false)
            if (uncompletedBlocks.count() > 1) {
                throw Exception("Too many active blocks. Should only get one")
            }
            if (uncompletedBlocks.isEmpty()) {
                activeBlockBtn.setOnClickListener {
                    inflateNewBlockPopupView()
                }
            }
            else {
                activeBlockBtn.text = uncompletedBlocks[0].name
                setBlockButtonOnClickListener(activeBlockBtn, uncompletedBlocks[0])
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
     * The button create a block and opens the [BlockActivity] for that block
     */
    private fun defineNewBlockPopupViewListeners() {
        val context = this
        createBlockBtn.setOnClickListener{
            val blockName = newBlockNameEt.text.toString().trim()

            if (blockName.isEmpty()){
                // TODO
                Log.d(TAG, "Print a message indicating that the block name is empty")
            }
            else {
                val dbInstance = ScarletDatabase.getInstance(this)
                val block = Block(name = blockName)

                lifecycleScope.launch(Dispatchers.IO) {
                    dbInstance.blockDao().insertBlock(block)
                    val intent = Intent(context, BlockActivity::class.java)
                    intent.putExtra("block", block)
                    startActivity(intent)
                }

            }
        }
    }

    /**
     * Gets all completed training blocks, and displays a Button for each.
     *
     * If no completed blocks are found, displays a TextView indicating so
     */
    private fun displayCompletedBlocksSection() {
        val dbInstance = ScarletDatabase.getInstance(this)
        val context = this

        lifecycleScope.launch(Dispatchers.IO) {
            val completedBlocks = dbInstance.blockDao().getBlocksByCompleted(true)
            if (completedBlocks.isEmpty()) {
                runOnUiThread {
                    val noCompletedBlocksTv = TextView(context)
                    noCompletedBlocksTv.text = NO_COMPLETED_BLOCK_MSG
                    addViewToConstraintLayout(noCompletedBlocksTv, R.id.completedBlocksTv)
                }
            } else {
                var topToBottomTargetId = R.id.completedBlocksTv
                for (block in completedBlocks) {
                    runOnUiThread {
                        val blockButton = Button(context)
                        blockButton.id = View.generateViewId()
                        blockButton.text = block.name
                        setBlockButtonOnClickListener(blockButton, block)
                        addViewToConstraintLayout(blockButton, topToBottomTargetId)
                        topToBottomTargetId = blockButton.id
                    }
                }
            }
        }
    }

    /**
     * Sets the onClick listener for [blockButton] to open a new [BlockActivity]
     *
     * @param blockButton Button to set the listener for
     * @param block block to send via the intent
     */
    private fun setBlockButtonOnClickListener(blockButton: Button, block: Block) {
        blockButton.setOnClickListener {
            val intent = Intent(this, BlockActivity::class.java)
            intent.putExtra("block", block)
            startActivity(intent)
        }
    }

    /**
     * Add [view] to the [constraintLayout]. Constrains the top side of the view to
     * the bottom side of the view identified by [topToBottomTargetId]
     *
     * @param view View to add to the [constraintLayout]
     * @param topToBottomTargetId Identifier of a target view. The top side of
     * the added view will be constrained to the bottom side of this target view
     */
    private fun addViewToConstraintLayout(view: View, topToBottomTargetId: Int) {
        view.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topToBottom = topToBottomTargetId
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            topMargin = dpToPx(32)
        }

        constraintLayout.addView(view)
    }
}