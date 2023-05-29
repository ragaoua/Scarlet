package com.example.scarlet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

private const val TAG = "BlockActivity"
private const val NO_SESSIONS_MSG = "No sessions found"


class BlockActivity : AppCompatActivity() {
    private lateinit var blockNameTv: TextView
    private lateinit var sessionsVLL: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_block)
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
        val sessions = getBlockSessions(block)

        if (sessions.isEmpty()) {
            val noSessionsTv = TextView(this)
            noSessionsTv.text = NO_SESSIONS_MSG
            sessionsVLL.addView(noSessionsTv)
        }
        else {
            for (session in sessions) {
                val sessionButton = Button(this)
                sessionButton.text = session.date
                sessionsVLL.addView(sessionButton)

                sessionButton.setOnClickListener {
                    val intent = Intent(this, SessionActivity::class.java)
                    intent.putExtra("session", session)
                    startActivity(intent)
                }
            }
        }
    }

    /**
     * @param block Block whose sessions will be returned
     *
     * @return List of session for the [block]
     */
    private fun getBlockSessions(block: Block): List<Session> {
        val dbHelper = ScarletDbHelper(this)
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM session WHERE block_id = ?", arrayOf(block.id.toString()))

        val sessions = ArrayList<Session>()
        if (cursor.moveToFirst()) {
            sessions.add(Session(cursor))
        }

        cursor.close()
        db.close()

        return sessions
    }
}