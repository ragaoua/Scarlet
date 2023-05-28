package com.example.scarlet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

private const val TAG = "ShowBlockActivity"

class ShowBlockActivity : AppCompatActivity() {
    private lateinit var blockIdTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_block)

        blockIdTv = findViewById(R.id.blockIdTv)

        val block = intent.getSerializableExtra("block") as Block

        Log.d(TAG, "Block is $block")
    }
}