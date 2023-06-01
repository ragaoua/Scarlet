package com.example.scarlet.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.scarlet.model.Block
import com.example.scarlet.R
import com.example.scarlet.ScarletDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "BlockActivity"
private const val NO_SESSIONS_MSG = "No sessions found"


class BlockActivity : AppCompatActivity() {
    private lateinit var blockNameTv: TextView
    private lateinit var sessionsVLL: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block)
        blockNameTv = findViewById(R.id.blockNameTv)
        sessionsVLL = findViewById(R.id.sessionsVLL)

        val block = intent.getSerializableExtra("block") as Block
        blockNameTv.text = block.name

        displaySessionsSection(block)
    }

    /**
     * Displays a button for each session of the [block].
     *
     * If no sessions are found, displays a TextView indicating so
     *
     * @param block Block whose sessions will be displayed
     */
    private fun displaySessionsSection(block: Block) {
        val dbInstance = ScarletDatabase.getInstance(this)
        val context = this

        lifecycleScope.launch(Dispatchers.IO) {
            val sessions = dbInstance.sessionDao().getSessionsByBlockId(block.id!!)

            if (sessions.isEmpty()) {
                runOnUiThread {
                    val noSessionsTv = TextView(context)
                    noSessionsTv.text = NO_SESSIONS_MSG
                    sessionsVLL.addView(noSessionsTv)
                }
            } else {
                for (session in sessions) {
                    runOnUiThread {
                        val sessionButton = Button(context)
                        sessionButton.text = session.date
                        sessionsVLL.addView(sessionButton)

                        sessionButton.setOnClickListener {
                            val intent = Intent(context, SessionActivity::class.java)
                            intent.putExtra("session", session)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}