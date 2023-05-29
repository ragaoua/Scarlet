package com.example.scarlet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

private const val TAG = "ShowBlockActivity"

class ShowBlockActivity : AppCompatActivity() {
    private lateinit var blockNameTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_block)

        val block = intent.getSerializableExtra("block") as Block

        blockNameTv = findViewById(R.id.blockNameTv)
        blockNameTv.text = block.name

        val sessions = getBlockSessions(block)

        Log.d(TAG, "Block : $block")
        Log.d(TAG, "Sessions : $sessions")
    }

    /**
     * @param block block whose sessions will be returned
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