package com.example.scarlet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class ShowBlockActivity : AppCompatActivity() {
    private lateinit var blockIdTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_block)
        blockIdTv = findViewById(R.id.blockIdTv)
        val blockId = intent.getStringExtra("blockId")

        Log.d("ShowBlockActivity", "blockId is $blockId")
    }
}