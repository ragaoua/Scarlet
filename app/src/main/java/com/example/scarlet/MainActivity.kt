package com.example.scarlet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var trainingLogBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trainingLogBtn = findViewById(R.id.trainingLogBtn)

        trainingLogBtn.setOnClickListener{
            val intent = Intent(this, TrainingLogsActivity::class.java)
            startActivity(intent)
        }

    }
}